package com.Trackdev.control.Dto;

import com.Trackdev.control.Entity.Enums.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
        @NotBlank @Size(max = 80) String nombre,
        @NotBlank @Size(max = 80) String apellido,
        @Email @NotBlank @Size(max = 150) String email,
        @NotBlank @Size(min = 8) String password,
        @NotNull RolUsuario rol,
        Long perfilId
) {
}