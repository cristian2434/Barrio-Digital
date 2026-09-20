package com.barriodigital.report.service;

import com.barriodigital.report.dto.KpiResponse;
import com.barriodigital.report.dto.TopProcedureResponse;
import com.barriodigital.report.model.EventoTramite;
import com.barriodigital.report.repository.EventoTramiteRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final EventoTramiteRepository repository;

    public ReportService(EventoTramiteRepository repository) {
        this.repository = repository;
    }

    /** Invocado por el listener de Kafka al llegar un evento nuevo. */
    public void registrarEvento(Long tramiteId, Long tipoTramiteId, String estado) {
        EventoTramite evento = new EventoTramite();
        evento.setTramiteId(tramiteId);
        evento.setTipoTramiteId(tipoTramiteId);
        evento.setEstado(estado);
        evento.setFecha(Instant.now());
        repository.save(evento);
    }

    public KpiResponse kpis(String range) {
        Instant desde = calcularDesde(range);
        List<EventoTramite> eventos = repository.findByFechaAfter(desde);

        long horas = Math.max(1, ChronoUnit.HOURS.between(desde, Instant.now()));
        long tramitesPorHora = eventos.size() / horas;

        List<EventoTramite> resueltos = eventos.stream()
                .filter(e -> "RESUELTO".equals(e.getEstado()))
                .toList();
        double tiempoPromedio = resueltos.isEmpty() ? 0.0 :
                resueltos.stream()
                        .mapToLong(e -> Duration.between(desde, e.getFecha()).toHours())
                        .average().orElse(0.0);

        Map<String, Long> estadosActivos = eventos.stream()
                .collect(Collectors.groupingBy(EventoTramite::getEstado, Collectors.counting()));

        return new KpiResponse(tramitesPorHora, tiempoPromedio, estadosActivos);
    }

    public List<TopProcedureResponse> topProcedures(String range) {
        Instant desde = calcularDesde(range);
        return repository.findByFechaAfter(desde).stream()
                .collect(Collectors.groupingBy(EventoTramite::getTipoTramiteId, Collectors.counting()))
                .entrySet().stream()
                .map(e -> new TopProcedureResponse(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(TopProcedureResponse::getCantidad).reversed())
                .toList();
    }

    private Instant calcularDesde(String range) {
        return switch (range == null ? "last24h" : range) {
            case "last7d" -> Instant.now().minus(7, ChronoUnit.DAYS);
            case "last30d" -> Instant.now().minus(30, ChronoUnit.DAYS);
            default -> Instant.now().minus(24, ChronoUnit.HOURS);
        };
    }
}
