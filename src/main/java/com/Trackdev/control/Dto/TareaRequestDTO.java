package com.Trackdev.control.Dto;

import com.Trackdev.control.Entity.Enums.EstadoTarea;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TareaRequestDTO(
        @NotNull Long moduloId,
        @NotBlank @Size(max = 150) String nombre,
        String descripcion,
        EstadoTarea estado
) {
}