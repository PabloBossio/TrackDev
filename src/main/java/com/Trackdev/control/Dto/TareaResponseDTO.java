package com.Trackdev.control.Dto;

import com.Trackdev.control.Entity.Enums.EstadoTarea;

import java.time.LocalDateTime;

public record TareaResponseDTO(
        Long id,
        ModuloResumenDTO modulo,
        String nombre,
        String descripcion,
        EstadoTarea estado,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}