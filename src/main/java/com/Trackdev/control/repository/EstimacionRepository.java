package com.Trackdev.control.repository;

import com.Trackdev.control.Entity.Estimacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstimacionRepository extends JpaRepository<Estimacion, Long> {

    boolean existsByTareaId(Long tareaId);

    Optional<Estimacion> findByTareaId(Long tareaId);
}