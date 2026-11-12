package com.Trackdev.control.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record RegistroTiempoResponseDTO(
        Long id,
        TareaResumenDTO tarea,
        UsuarioResumenDTO usuario,
        LocalDate fecha,
        BigDecimal horasTrabajadas,
        BigDecimal costoHoraAplicado,
        BigDecimal costoReal,
        String descripcion,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}