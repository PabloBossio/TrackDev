package com.Trackdev.control.Mapper;

import com.Trackdev.control.Entity.PerfilDesarrollador;
import com.Trackdev.control.Dto.PerfilDesarrolladorRequestDTO;
import com.Trackdev.control.Dto.PerfilDesarrolladorResponseDTO;
import com.Trackdev.control.Dto.PerfilDesarrolladorResumenDTO;

public final class PerfilDesarrolladorMapper {

    private PerfilDesarrolladorMapper() {
    }

    public static PerfilDesarrollador toEntity(PerfilDesarrolladorRequestDTO dto) {
        return PerfilDesarrollador.builder()
                .nombre(dto.nombre())
                .costoHora(dto.costoHora())
                .build();
    }

    public static void updateEntity(PerfilDesarrollador perfil, PerfilDesarrolladorRequestDTO dto) {
        perfil.setNombre(dto.nombre());
        perfil.setCostoHora(dto.costoHora());
    }

    public static PerfilDesarrolladorResponseDTO toResponseDTO(PerfilDesarrollador perfil) {
        return new PerfilDesarrolladorResponseDTO(
                perfil.getId(),
                perfil.getNombre(),
                perfil.getCostoHora(),
                perfil.isActivo(),
                perfil.getCreatedAt(),
                perfil.getUpdatedAt()
        );
    }

    public static PerfilDesarrolladorResumenDTO toResumenDTO(PerfilDesarrollador perfil) {
        return new PerfilDesarrolladorResumenDTO(
                perfil.getId(),
                perfil.getNombre()
        );
    }
}