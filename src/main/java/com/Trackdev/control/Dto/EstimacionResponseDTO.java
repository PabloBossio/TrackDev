package com.Trackdev.control.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EstimacionResponseDTO(
        Long id,
        TareaResumenDTO tarea,
        PerfilDesarrolladorResumenDTO perfilEstimado,
        BigDecimal horasEstimadas,
        BigDecimal costoEstimado,
        UsuarioResumenDTO creadoPor,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}