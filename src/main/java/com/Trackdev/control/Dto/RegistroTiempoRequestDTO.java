package com.Trackdev.control.Dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistroTiempoRequestDTO(
        @NotNull Long tareaId,
        Long usuarioId,
        @NotNull @PastOrPresent LocalDate fecha,
        @NotNull @Positive @DecimalMax("24.00") @Digits(integer = 2, fraction = 2) BigDecimal horasTrabajadas,
        @Size(max = 300) String descripcion
) {
}