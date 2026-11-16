package com.Trackdev.control.Repository;

import com.Trackdev.control.Entity.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuloRepository extends JpaRepository<Modulo, Long> {

    List<Modulo> findByProyectoId(Long proyectoId);

    boolean existsByProyectoIdAndNombre(Long proyectoId, String nombre);

    boolean existsByProyectoIdAndNombreAndIdNot(Long proyectoId, String nombre, Long id);
}