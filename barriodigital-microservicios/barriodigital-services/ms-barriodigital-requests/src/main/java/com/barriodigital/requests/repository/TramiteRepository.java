package com.barriodigital.requests.repository;

import com.barriodigital.requests.model.EstadoTramite;
import com.barriodigital.requests.model.Tramite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TramiteRepository extends JpaRepository<Tramite, Long> {

    List<Tramite> findByEstado(EstadoTramite estado);

    List<Tramite> findByVecinoUsername(String vecinoUsername);

    List<Tramite> findByFechaCreacionBetween(LocalDateTime from, LocalDateTime to);

    List<Tramite> findByEstadoAndFechaCreacionBetween(EstadoTramite estado, LocalDateTime from, LocalDateTime to);
}
