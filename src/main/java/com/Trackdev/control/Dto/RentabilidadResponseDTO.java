package com.Trackdev.control.Dto;

import java.math.BigDecimal;

public record RentabilidadResponseDTO(
        Long proyectoId,
        String nombreProyecto,
        BigDecimal presupuestoVenta,
        BigDecimal horasEstimadas,
        BigDecimal costoEstimado,
        BigDecimal rentabilidadEstimada,
        BigDecimal margenEstimadoPorcentaje,
        BigDecimal horasReales,
        BigDecimal costoReal,
        BigDecimal rentabilidadReal,
        BigDecimal margenRealPorcentaje
) {
}