package com.Trackdev.control.mapper;

import com.Trackdev.control.Entity.Cliente;
import com.Trackdev.control.Entity.Proyecto;
import com.Trackdev.control.dto.ProyectoRequestDTO;
import com.Trackdev.control.dto.ProyectoResponseDTO;
import com.Trackdev.control.dto.ProyectoResumenDTO;

public final class ProyectoMapper {

    private ProyectoMapper() {
    }

    public static Proyecto toEntity(ProyectoRequestDTO dto, Cliente cliente) {
        Proyecto proyecto = Proyecto.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .cliente(cliente)
                .fechaInicio(dto.fechaInicio())
                .fechaFinEstimada(dto.fechaFinEstimada())
                .presupuestoVenta(dto.presupuestoVenta())
                .build();
        if (dto.estado() != null) {
            proyecto.setEstado(dto.estado());
        }
        return proyecto;
    }

    public static void updateEntity(Proyecto proyecto, ProyectoRequestDTO dto, Cliente cliente) {
        proyecto.setNombre(dto.nombre());
        proyecto.setDescripcion(dto.descripcion());
        proyecto.setCliente(cliente);
        proyecto.setFechaInicio(dto.fechaInicio());
        proyecto.setFechaFinEstimada(dto.fechaFinEstimada());
        proyecto.setPresupuestoVenta(dto.presupuestoVenta());
        if (dto.estado() != null) {
            proyecto.setEstado(dto.estado());
        }
    }

    public static ProyectoResponseDTO toResponseDTO(Proyecto proyecto) {
        return new ProyectoResponseDTO(
                proyecto.getId(),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                ClienteMapper.toResumenDTO(proyecto.getCliente()),
                proyecto.getFechaInicio(),
                proyecto.getFechaFinEstimada(),
                proyecto.getEstado(),
                proyecto.getPresupuestoVenta(),
                proyecto.getCreatedAt(),
                proyecto.getUpdatedAt()
        );
    }

    public static ProyectoResumenDTO toResumenDTO(Proyecto proyecto) {
        return new ProyectoResumenDTO(
                proyecto.getId(),
                proyecto.getNombre()
        );
    }
}