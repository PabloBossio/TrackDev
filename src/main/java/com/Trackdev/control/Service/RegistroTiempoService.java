package com.Trackdev.control.Service;

import com.Trackdev.control.Dto.RegistroTiempoRequestDTO;
import com.Trackdev.control.Dto.RegistroTiempoResponseDTO;
import com.Trackdev.control.Entity.Enums.EstadoProyecto;
import com.Trackdev.control.Entity.Enums.EstadoTarea;
import com.Trackdev.control.Entity.Enums.RolUsuario;
import com.Trackdev.control.Entity.PerfilDesarrollador;
import com.Trackdev.control.Entity.RegistroTiempo;
import com.Trackdev.control.Entity.Tarea;
import com.Trackdev.control.Entity.Usuario;
import com.Trackdev.control.Exception.BusinessRuleException;
import com.Trackdev.control.Exception.ResourceNotFoundException;
import com.Trackdev.control.Mapper.RegistroTiempoMapper;
import com.Trackdev.control.Repository.EstimacionRepository;
import com.Trackdev.control.Repository.RegistroTiempoRepository;
import com.Trackdev.control.Repository.TareaRepository;
import com.Trackdev.control.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistroTiempoService {

    private final RegistroTiempoRepository registroTiempoRepository;
    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstimacionRepository estimacionRepository;

    public RegistroTiempoResponseDTO crear(RegistroTiempoRequestDTO dto) {
        Tarea tarea = buscarTareaCargable(dto.tareaId());

        // RN-01: no se puede cargar tiempo real sin estimación previa
        if (!estimacionRepository.existsByTareaId(tarea.getId())) {
            throw new BusinessRuleException(
                    "No se pueden cargar horas en la tarea " + tarea.getId() + " porque no tiene estimación");
        }

        Usuario usuario = buscarOperadorActivo(dto.usuarioId());
        BigDecimal costoHoraAplicado = usuario.getPerfil().getCostoHora();
        BigDecimal costoReal = calcularCostoReal(dto.horasTrabajadas(), costoHoraAplicado);

        RegistroTiempo registro = RegistroTiempoMapper.toEntity(dto, tarea, usuario, costoHoraAplicado, costoReal);
        registro = registroTiempoRepository.save(registro);

        if (tarea.getEstado() == EstadoTarea.PENDIENTE) {
            tarea.setEstado(EstadoTarea.EN_CURSO);
        }
        return RegistroTiempoMapper.toResponseDTO(registro);
    }

    @Transactional(readOnly = true)
    public List<RegistroTiempoResponseDTO> listarPorTarea(Long tareaId) {
        if (!tareaRepository.existsById(tareaId)) {
            throw new ResourceNotFoundException("Tarea no encontrada: id=" + tareaId);
        }
        return registroTiempoRepository.findByTareaId(tareaId).stream()
                .map(RegistroTiempoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RegistroTiempoResponseDTO> listarPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario no encontrado: id=" + usuarioId);
        }
        return registroTiempoRepository.findByUsuarioId(usuarioId).stream()
                .map(RegistroTiempoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public RegistroTiempoResponseDTO obtenerPorId(Long id) {
        return RegistroTiempoMapper.toResponseDTO(buscarOLanzar(id));
    }

    public RegistroTiempoResponseDTO actualizar(Long id, RegistroTiempoRequestDTO dto) {
        RegistroTiempo registro = buscarOLanzar(id);
        if (!registro.getTarea().getId().equals(dto.tareaId())) {
            throw new BusinessRuleException("No se puede mover un registro de horas a otra tarea");
        }
        buscarTareaCargable(dto.tareaId());

        // se recalcula con el MISMO costo/hora congelado al momento de la carga original
        BigDecimal costoReal = calcularCostoReal(dto.horasTrabajadas(), registro.getCostoHoraAplicado());
        RegistroTiempoMapper.updateEntity(registro, dto, costoReal);
        return RegistroTiempoMapper.toResponseDTO(registro);
    }

    public void eliminar(Long id) {
        RegistroTiempo registro = buscarOLanzar(id);
        buscarTareaCargable(registro.getTarea().getId());
        registroTiempoRepository.delete(registro);
    }

    private BigDecimal calcularCostoReal(BigDecimal horas, BigDecimal costoHora) {
        return horas.multiply(costoHora).setScale(2, RoundingMode.HALF_UP);
    }

    private RegistroTiempo buscarOLanzar(Long id) {
        return registroTiempoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de tiempo no encontrado: id=" + id));
    }

    private Tarea buscarTareaCargable(Long tareaId) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada: id=" + tareaId));
        EstadoProyecto estadoProyecto = tarea.getModulo().getProyecto().getEstado();
        if (estadoProyecto == EstadoProyecto.CANCELADO || estadoProyecto == EstadoProyecto.FINALIZADO) {
            throw new BusinessRuleException("El proyecto de la tarea está " + estadoProyecto + " y no admite cargas");
        }
        if (tarea.getEstado() == EstadoTarea.FINALIZADA) {
            throw new BusinessRuleException("La tarea " + tareaId + " está FINALIZADA y no admite cargas");
        }
        return tarea;
    }

    private Usuario buscarOperadorActivo(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: id=" + usuarioId));
        if (!usuario.isActivo()) {
            throw new BusinessRuleException("El usuario " + usuarioId + " está dado de baja");
        }
        if (usuario.getRol() != RolUsuario.OPERADOR) {
            throw new BusinessRuleException("Solo un operador puede cargar horas");
        }
        PerfilDesarrollador perfil = usuario.getPerfil();
        if (perfil == null) {
            throw new BusinessRuleException("El operador " + usuarioId + " no tiene perfil asignado");
        }
        return usuario;
    }
}