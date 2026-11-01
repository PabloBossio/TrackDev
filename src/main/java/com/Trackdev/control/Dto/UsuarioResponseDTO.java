package com.Trackdev.control.Dto;

import com.Trackdev.control.Entity.Enums.RolUsuario;

import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String nombre,
        String apellido,
        String email,
        RolUsuario rol,
        PerfilDesarrolladorResumenDTO perfil,
        boolean activo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}