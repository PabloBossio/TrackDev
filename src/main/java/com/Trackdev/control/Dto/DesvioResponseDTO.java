package com.Trackdev.control.Dto;

import java.math.BigDecimal;

public record DesvioResponseDTO(
        Long id,
        String nombre,
        BigDecimal horasEstimadas,
        BigDecimal horasReales,
        BigDecimal desvioHoras,
        BigDecimal porcentajeConsumidoHoras,
        BigDecimal costoEstimado,
        BigDecimal costoReal,
        BigDecimal desvioCosto,
        BigDecimal porcentajeConsumidoCosto
) {
}