package com.Trackdev.control.Dto;

public record LoginResponseDTO(
        String token,
        UsuarioResponseDTO usuario
) {
}