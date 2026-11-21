package com.Trackdev.control.Repository;

import com.Trackdev.control.Entity.PerfilDesarrollador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilDesarrolladorRepository extends JpaRepository<PerfilDesarrollador, Long> {

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, Long id);
}