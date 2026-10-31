package com.Trackdev.control.dto;

import java.time.LocalDateTime;

public record ModuloResponseDTO(
        Long id,
        ProyectoResumenDTO proyecto,
        String nombre,
        String descripcion,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}