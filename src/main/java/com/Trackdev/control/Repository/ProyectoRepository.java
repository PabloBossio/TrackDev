package com.Trackdev.control.Repository;

import com.Trackdev.control.Entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findByClienteId(Long clienteId);
}