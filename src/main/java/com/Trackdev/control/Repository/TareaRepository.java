package com.Trackdev.control.Repository;

import com.Trackdev.control.Entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByModuloId(Long moduloId);

    boolean existsByModuloId(Long moduloId);

    @Query("""
            SELECT t FROM Tarea t
            WHERE EXISTS (SELECT 1 FROM Estimacion e WHERE e.tarea = t)
            AND t.estado <> com.Trackdev.control.Entity.Enums.EstadoTarea.FINALIZADA
            AND t.modulo.proyecto.estado NOT IN (
                com.Trackdev.control.Entity.Enums.EstadoProyecto.CANCELADO,
                com.Trackdev.control.Entity.Enums.EstadoProyecto.FINALIZADO
            )
            """)
    List<Tarea> findDisponiblesParaCargar();
}