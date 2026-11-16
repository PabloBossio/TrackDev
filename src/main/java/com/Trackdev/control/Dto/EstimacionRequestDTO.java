package com.Trackdev.control.Dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record EstimacionRequestDTO(
        @NotNull Long perfilEstimadoId,
        @NotNull @Positive @Digits(integer = 6, fraction = 2) BigDecimal horasEstimadas,
        @NotNull Long creadoPorId
) {
}