package com.Trackdev.control.Dto;

import java.math.BigDecimal;

public record AlertaDTO(
        String tipo,
        Long id,
        String nombre,
        String metrica,
        BigDecimal porcentajeConsumido,
        String nivel
) {}