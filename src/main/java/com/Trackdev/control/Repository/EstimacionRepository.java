package com.Trackdev.control.Repository;

import com.Trackdev.control.Entity.Estimacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface EstimacionRepository extends JpaRepository<Estimacion, Long> {

    boolean existsByTareaId(Long tareaId);

    Optional<Estimacion> findByTareaId(Long tareaId);

    @Query("SELECT COALESCE(SUM(e.horasEstimadas), 0) FROM Estimacion e WHERE e.tarea.modulo.proyecto.id = :proyectoId")
    BigDecimal sumHorasEstimadasPorProyecto(Long proyectoId);

    @Query("SELECT COALESCE(SUM(e.costoEstimado), 0) FROM Estimacion e WHERE e.tarea.modulo.proyecto.id = :proyectoId")
    BigDecimal sumCostoEstimadoPorProyecto(Long proyectoId);
}