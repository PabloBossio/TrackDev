package com.Trackdev.control.Mapper;

import com.Trackdev.control.Dto.EstimacionRequestDTO;
import com.Trackdev.control.Dto.EstimacionResponseDTO;
import com.Trackdev.control.Entity.Estimacion;
import com.Trackdev.control.Entity.PerfilDesarrollador;
import com.Trackdev.control.Entity.Tarea;
import com.Trackdev.control.Entity.Usuario;

import java.math.BigDecimal;

public final class EstimacionMapper {

    private EstimacionMapper() {
    }

    public static Estimacion toEntity(EstimacionRequestDTO dto,
                                      Tarea tarea,
                                      PerfilDesarrollador perfil,
                                      Usuario creadoPor,
                                      BigDecimal costoEstimado) {
        return Estimacion.builder()
                .tarea(tarea)
                .perfilEstimado(perfil)
                .horasEstimadas(dto.horasEstimadas())
                .costoEstimado(costoEstimado)
                .creadoPor(creadoPor)
                .build();
    }

    public static void updateEntity(Estimacion estimacion,
                                    EstimacionRequestDTO dto,
                                    PerfilDesarrollador perfil,
                                    BigDecimal costoEstimado) {
        estimacion.setPerfilEstimado(perfil);
        estimacion.setHorasEstimadas(dto.horasEstimadas());
        estimacion.setCostoEstimado(costoEstimado);
    }

    public static EstimacionResponseDTO toResponseDTO(Estimacion estimacion) {
        return new EstimacionResponseDTO(
                estimacion.getId(),
                TareaMapper.toResumenDTO(estimacion.getTarea()),
                PerfilDesarrolladorMapper.toResumenDTO(estimacion.getPerfilEstimado()),
                estimacion.getHorasEstimadas(),
                estimacion.getCostoEstimado(),
                UsuarioMapper.toResumenDTO(estimacion.getCreadoPor()),
                estimacion.getCreatedAt(),
                estimacion.getUpdatedAt()
        );
    }
}