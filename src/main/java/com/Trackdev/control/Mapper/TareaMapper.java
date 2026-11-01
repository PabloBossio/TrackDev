package com.Trackdev.control.Mapper;

import com.Trackdev.control.Entity.Modulo;
import com.Trackdev.control.Entity.Tarea;
import com.Trackdev.control.Dto.TareaRequestDTO;
import com.Trackdev.control.Dto.TareaResponseDTO;
import com.Trackdev.control.Dto.TareaResumenDTO;

public final class TareaMapper {

    private TareaMapper() {
    }

    public static Tarea toEntity(TareaRequestDTO dto, Modulo modulo) {
        Tarea tarea = Tarea.builder()
                .modulo(modulo)
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .build();
        if (dto.estado() != null) {
            tarea.setEstado(dto.estado());
        }
        return tarea;
    }

    public static void updateEntity(Tarea tarea, TareaRequestDTO dto, Modulo modulo) {
        tarea.setModulo(modulo);
        tarea.setNombre(dto.nombre());
        tarea.setDescripcion(dto.descripcion());
        if (dto.estado() != null) {
            tarea.setEstado(dto.estado());
        }
    }

    public static TareaResponseDTO toResponseDTO(Tarea tarea) {
        return new TareaResponseDTO(
                tarea.getId(),
                ModuloMapper.toResumenDTO(tarea.getModulo()),
                tarea.getNombre(),
                tarea.getDescripcion(),
                tarea.getEstado(),
                tarea.getCreatedAt(),
                tarea.getUpdatedAt()
        );
    }

    public static TareaResumenDTO toResumenDTO(Tarea tarea) {
        return new TareaResumenDTO(
                tarea.getId(),
                tarea.getNombre()
        );
    }
}