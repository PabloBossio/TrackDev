package com.Trackdev.control.Repository;

import com.Trackdev.control.Entity.RegistroTiempo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface RegistroTiempoRepository extends JpaRepository<RegistroTiempo, Long> {

    List<RegistroTiempo> findByTareaId(Long tareaId);

    List<RegistroTiempo> findByUsuarioId(Long usuarioId);

    boolean existsByTareaId(Long tareaId);

    @Query("SELECT COALESCE(SUM(r.horasTrabajadas), 0) FROM RegistroTiempo r WHERE r.tarea.modulo.proyecto.id = :proyectoId")
    BigDecimal sumHorasTrabajadasPorProyecto(Long proyectoId);

    @Query("SELECT COALESCE(SUM(r.costoReal), 0) FROM RegistroTiempo r WHERE r.tarea.modulo.proyecto.id = :proyectoId")
    BigDecimal sumCostoRealPorProyecto(Long proyectoId);

    @Query("SELECT COALESCE(SUM(r.horasTrabajadas), 0) FROM RegistroTiempo r WHERE r.tarea.modulo.id = :moduloId")
    BigDecimal sumHorasTrabajadasPorModulo(Long moduloId);

    @Query("SELECT COALESCE(SUM(r.costoReal), 0) FROM RegistroTiempo r WHERE r.tarea.modulo.id = :moduloId")
    BigDecimal sumCostoRealPorModulo(Long moduloId);
}