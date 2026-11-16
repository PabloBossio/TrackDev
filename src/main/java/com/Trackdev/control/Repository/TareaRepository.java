package com.Trackdev.control.Repository;

import com.Trackdev.control.Entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByModuloId(Long moduloId);

    boolean existsByModuloId(Long moduloId);
}