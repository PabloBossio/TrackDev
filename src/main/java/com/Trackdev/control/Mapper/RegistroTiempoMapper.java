package com.Trackdev.control.Mapper;

import com.Trackdev.control.Dto.RegistroTiempoRequestDTO;
import com.Trackdev.control.Dto.RegistroTiempoResponseDTO;
import com.Trackdev.control.Entity.RegistroTiempo;
import com.Trackdev.control.Entity.Tarea;
import com.Trackdev.control.Entity.Usuario;

import java.math.BigDecimal;

public final class RegistroTiempoMapper {

    private RegistroTiempoMapper() {
    }

    public static RegistroTiempo toEntity(RegistroTiempoRequestDTO dto,
                                          Tarea tarea,
                                          Usuario usuario,
                                          BigDecimal costoHoraAplicado,
                                          BigDecimal costoReal) {
        return RegistroTiempo.builder()
                .tarea(tarea)
                .usuario(usuario)
                .fecha(dto.fecha())
                .horasTrabajadas(dto.horasTrabajadas())
                .costoHoraAplicado(costoHoraAplicado)
                .costoReal(costoReal)
                .descripcion(dto.descripcion())
                .build();
    }

    public static void updateEntity(RegistroTiempo registro,
                                    RegistroTiempoRequestDTO dto,
                                    BigDecimal costoReal) {
        registro.setFecha(dto.fecha());
        registro.setHorasTrabajadas(dto.horasTrabajadas());
        registro.setCostoReal(costoReal);
        registro.setDescripcion(dto.descripcion());
    }

    public static RegistroTiempoResponseDTO toResponseDTO(RegistroTiempo registro) {
        return new RegistroTiempoResponseDTO(
                registro.getId(),
                TareaMapper.toResumenDTO(registro.getTarea()),
                UsuarioMapper.toResumenDTO(registro.getUsuario()),
                registro.getFecha(),
                registro.getHorasTrabajadas(),
                registro.getCostoHoraAplicado(),
                registro.getCostoReal(),
                registro.getDescripcion(),
                registro.getCreatedAt(),
                registro.getUpdatedAt()
        );
    }
}