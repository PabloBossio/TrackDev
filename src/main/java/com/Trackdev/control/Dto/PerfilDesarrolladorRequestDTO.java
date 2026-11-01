package com.Trackdev.control.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PerfilDesarrolladorRequestDTO(
        @NotBlank @Size(max = 80) String nombre,
        @NotNull @DecimalMin(value = "0.01") BigDecimal costoHora
) {
}