package com.Trackdev.control.dto;

import com.Trackdev.control.Entity.Enums.EstadoProyecto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProyectoResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        ClienteResumenDTO cliente,
        LocalDate fechaInicio,
        LocalDate fechaFinEstimada,
        EstadoProyecto estado,
        BigDecimal presupuestoVenta,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
