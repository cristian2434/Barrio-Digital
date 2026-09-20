package com.barriodigital.requests.service;

import com.barriodigital.requests.client.CatalogClient;
import com.barriodigital.requests.dto.CrearTramiteRequest;
import com.barriodigital.requests.dto.TramiteResponse;
import com.barriodigital.requests.event.TramiteEventPublisher;
import com.barriodigital.requests.exception.EstadoInvalidoException;
import com.barriodigital.requests.exception.TramiteNoEncontradoException;
import com.barriodigital.requests.model.EstadoTramite;
import com.barriodigital.requests.model.Tramite;
import com.barriodigital.requests.repository.TramiteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@Service
public class TramiteService {

    private final TramiteRepository repository;
    private final TramiteEventPublisher eventPublisher;
    private final CatalogClient catalogClient;

    // Transiciones permitidas: sección 3 del caso.
    // "No se puede pasar a EN_TERRENO sin ADMITIR" -> EN_GESTION es el paso intermedio obligatorio.
    private static final Map<EstadoTramite, EnumSet<EstadoTramite>> TRANSICIONES_PERMITIDAS = new EnumMap<>(EstadoTramite.class);
    static {
        TRANSICIONES_PERMITIDAS.put(EstadoTramite.INGRESADO, EnumSet.of(EstadoTramite.ADMITIDO, EstadoTramite.RECHAZADO));
        TRANSICIONES_PERMITIDAS.put(EstadoTramite.ADMITIDO, EnumSet.of(EstadoTramite.EN_GESTION, EstadoTramite.RECHAZADO));
        TRANSICIONES_PERMITIDAS.put(EstadoTramite.EN_GESTION, EnumSet.of(EstadoTramite.EN_TERRENO, EstadoTramite.RECHAZADO));
        TRANSICIONES_PERMITIDAS.put(EstadoTramite.EN_TERRENO, EnumSet.of(EstadoTramite.RESUELTO, EstadoTramite.RECHAZADO));
        TRANSICIONES_PERMITIDAS.put(EstadoTramite.RESUELTO, EnumSet.noneOf(EstadoTramite.class));
        TRANSICIONES_PERMITIDAS.put(EstadoTramite.RECHAZADO, EnumSet.noneOf(EstadoTramite.class));
    }

    public TramiteService(TramiteRepository repository, TramiteEventPublisher eventPublisher, CatalogClient catalogClient) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.catalogClient = catalogClient;
    }

    public TramiteResponse crear(CrearTramiteRequest request, String vecinoUsername) {
        Tramite tramite = new Tramite();
        tramite.setTipoTramiteId(request.getTipoTramiteId());
        tramite.setDescripcion(request.getDescripcion());
        tramite.setVecinoUsername(vecinoUsername);
        tramite.setEstado(EstadoTramite.INGRESADO);
        Tramite guardado = repository.save(tramite);

        eventPublisher.publicarCambioEstado(guardado, null, vecinoUsername);
        eventPublisher.publicarComprobanteIngreso(guardado);

        return TramiteResponse.fromEntity(guardado);
    }

    public TramiteResponse buscarPorId(Long id) {
        return TramiteResponse.fromEntity(obtenerOFallar(id));
    }

    public List<TramiteResponse> listar(EstadoTramite estado, LocalDateTime from, LocalDateTime to) {
        List<Tramite> resultado;
        if (estado != null && from != null && to != null) {
            resultado = repository.findByEstadoAndFechaCreacionBetween(estado, from, to);
        } else if (estado != null) {
            resultado = repository.findByEstado(estado);
        } else if (from != null && to != null) {
            resultado = repository.findByFechaCreacionBetween(from, to);
        } else {
            resultado = repository.findAll();
        }
        return resultado.stream().map(TramiteResponse::fromEntity).toList();
    }

    public TramiteResponse cambiarEstado(Long id, EstadoTramite nuevoEstado, String funcionario) {
        Tramite tramite = obtenerOFallar(id);

        EnumSet<EstadoTramite> permitidos = TRANSICIONES_PERMITIDAS.get(tramite.getEstado());
        if (permitidos == null || !permitidos.contains(nuevoEstado)) {
            throw new EstadoInvalidoException(
                    "No se puede pasar de " + tramite.getEstado() + " a " + nuevoEstado +
                    ". Transiciones válidas desde " + tramite.getEstado() + ": " + permitidos);
        }

        EstadoTramite estadoAnterior = tramite.getEstado();
        tramite.setEstado(nuevoEstado);
        tramite.setFechaActualizacion(LocalDateTime.now());
        if (funcionario != null) {
            tramite.setFuncionarioAsignado(funcionario);
        }

        if (nuevoEstado == EstadoTramite.ADMITIDO) {
            // No revertimos el cambio de estado si catalog está caído: queda logueado
            // dentro de CatalogClient y es un problema de infraestructura, no de negocio.
            catalogClient.descontarCupo(tramite.getTipoTramiteId());
        }

        Tramite guardado = repository.save(tramite);

        // Kafka: todo cambio de estado se publica para auditoría y reportería.
        eventPublisher.publicarCambioEstado(guardado, estadoAnterior, funcionario);

        // RabbitMQ: notificaciones puntuales según el nuevo estado.
        if (nuevoEstado == EstadoTramite.EN_TERRENO) {
            eventPublisher.publicarTicketCuadrilla(guardado);
        } else if (nuevoEstado == EstadoTramite.RESUELTO) {
            eventPublisher.publicarNotificacionResuelto(guardado);
        }

        return TramiteResponse.fromEntity(guardado);
    }

    private Tramite obtenerOFallar(Long id) {
        return repository.findById(id).orElseThrow(() -> new TramiteNoEncontradoException(id));
    }
}
