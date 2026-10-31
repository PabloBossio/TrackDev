package com.Trackdev.control.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ModuloRequestDTO(
        @NotNull Long proyectoId,
        @NotBlank @Size(max = 120) String nombre,
        String descripcion
) {
}