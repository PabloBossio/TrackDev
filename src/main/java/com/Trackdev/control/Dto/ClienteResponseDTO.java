package com.Trackdev.control.Dto;

import java.time.LocalDateTime;

public record ClienteResponseDTO(
        Long id,
        String razonSocial,
        String cuit,
        String contactoNombre,
        String contactoEmail,
        String contactoTelefono,
        boolean activo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}