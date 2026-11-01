package com.Trackdev.control.Mapper;

import com.Trackdev.control.Entity.Modulo;
import com.Trackdev.control.Entity.Proyecto;
import com.Trackdev.control.Dto.ModuloRequestDTO;
import com.Trackdev.control.Dto.ModuloResponseDTO;
import com.Trackdev.control.Dto.ModuloResumenDTO;

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