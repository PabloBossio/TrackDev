package com.Trackdev.control.Dto;

import com.Trackdev.control.Entity.Enums.EstadoProyecto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProyectoRequestDTO(
        @NotBlank @Size(max = 150) String nombre,
        String descripcion,
        @NotNull Long clienteId,
        @NotNull LocalDate fechaInicio,
        LocalDate fechaFinEstimada,
        EstadoProyecto estado,
        @Positive BigDecimal presupuestoVenta
) {
}