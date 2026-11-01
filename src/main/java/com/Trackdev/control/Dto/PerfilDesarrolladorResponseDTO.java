package com.Trackdev.control.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PerfilDesarrolladorResponseDTO(
        Long id,
        String nombre,
        BigDecimal costoHora,
        boolean activo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}