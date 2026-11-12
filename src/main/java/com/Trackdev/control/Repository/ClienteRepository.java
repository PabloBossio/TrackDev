package com.Trackdev.control.Repository;

import com.Trackdev.control.Entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCuit(String cuit);

    boolean existsByCuitAndIdNot(String cuit, Long id);
}