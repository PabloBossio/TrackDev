package com.Trackdev.control.mapper;

import com.Trackdev.control.Entity.Modulo;
import com.Trackdev.control.Entity.Proyecto;
import com.Trackdev.control.dto.ModuloRequestDTO;
import com.Trackdev.control.dto.ModuloResponseDTO;
import com.Trackdev.control.dto.ModuloResumenDTO;

public final class ModuloMapper {

    private ModuloMapper() {
    }

    public static Modulo toEntity(ModuloRequestDTO dto, Proyecto proyecto) {
        return Modulo.builder()
                .proyecto(proyecto)
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .build();
    }

    public static void updateEntity(Modulo modulo, ModuloRequestDTO dto, Proyecto proyecto) {
        modulo.setProyecto(proyecto);
        modulo.setNombre(dto.nombre());
        modulo.setDescripcion(dto.descripcion());
    }

    public static ModuloResponseDTO toResponseDTO(Modulo modulo) {
        return new ModuloResponseDTO(
                modulo.getId(),
                ProyectoMapper.toResumenDTO(modulo.getProyecto()),
                modulo.getNombre(),
                modulo.getDescripcion(),
                modulo.getCreatedAt(),
                modulo.getUpdatedAt()
        );
    }

    public static ModuloResumenDTO toResumenDTO(Modulo modulo) {
        return new ModuloResumenDTO(
                modulo.getId(),
                modulo.getNombre()
        );
    }
}