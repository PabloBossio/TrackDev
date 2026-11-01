package com.Trackdev.control.Mapper;

import com.Trackdev.control.Entity.PerfilDesarrollador;
import com.Trackdev.control.Entity.Usuario;
import com.Trackdev.control.Dto.UsuarioRequestDTO;
import com.Trackdev.control.Dto.UsuarioResponseDTO;
import com.Trackdev.control.Dto.UsuarioResumenDTO;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario toEntity(UsuarioRequestDTO dto, PerfilDesarrollador perfil, String passwordHash) {
        return Usuario.builder()
                .nombre(dto.nombre())
                .apellido(dto.apellido())
                .email(dto.email())
                .passwordHash(passwordHash)
                .rol(dto.rol())
                .perfil(perfil)
                .build();
    }

    public static void updateEntity(Usuario usuario, UsuarioRequestDTO dto, PerfilDesarrollador perfil) {
        usuario.setNombre(dto.nombre());
        usuario.setApellido(dto.apellido());
        usuario.setEmail(dto.email());
        usuario.setRol(dto.rol());
        usuario.setPerfil(perfil);
    }

    public static UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getPerfil() != null ? PerfilDesarrolladorMapper.toResumenDTO(usuario.getPerfil()) : null,
                usuario.isActivo(),
                usuario.getCreatedAt(),
                usuario.getUpdatedAt()
        );
    }

    public static UsuarioResumenDTO toResumenDTO(Usuario usuario) {
        return new UsuarioResumenDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail()
        );
    }
}