package com.Trackdev.control.repository;

import com.Trackdev.control.Entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
}