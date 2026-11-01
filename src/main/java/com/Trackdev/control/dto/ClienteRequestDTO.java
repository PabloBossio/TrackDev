package com.Trackdev.control.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequestDTO(
        @NotBlank @Size(max = 150) String razonSocial,
        @Size(max = 20) String cuit,
        @Size(max = 120) String contactoNombre,
        @Email @Size(max = 150) String contactoEmail,
        @Size(max = 30) String contactoTelefono
) {
}