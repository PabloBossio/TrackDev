package com.Trackdev.control.Service;

import com.Trackdev.control.Dto.EstimacionRequestDTO;
import com.Trackdev.control.Dto.EstimacionResponseDTO;
import com.Trackdev.control.Entity.Enums.EstadoProyecto;
import com.Trackdev.control.Entity.Enums.RolUsuario;
import com.Trackdev.control.Entity.Estimacion;
import com.Trackdev.control.Entity.PerfilDesarrollador;
import com.Trackdev.control.Entity.Tarea;
import com.Trackdev.control.Entity.Usuario;
import com.Trackdev.control.Exception.BusinessRuleException;
import com.Trackdev.control.Exception.ResourceNotFoundException;
import com.Trackdev.control.Mapper.EstimacionMapper;
import com.Trackdev.control.Repository.EstimacionRepository;
import com.Trackdev.control.Repository.PerfilDesarrolladorRepository;
import com.Trackdev.control.Repository.RegistroTiempoRepository;
import com.Trackdev.control.Repository.TareaRepository;
import com.Trackdev.control.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Transactional
public class EstimacionService {

    private final EstimacionRepository estimacionRepository;
    private final TareaRepository tareaRepository;
    private final PerfilDesarrolladorRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final RegistroTiempoRepository registroTiempoRepository;

    public EstimacionResponseDTO crear(Long tareaId, EstimacionRequestDTO dto) {
        Tarea tarea = buscarTareaConProyectoAbierto(tareaId);
        if (estimacionRepository.existsByTareaId(tareaId)) {
            throw new BusinessRuleException("La tarea " + tareaId + " ya tiene una estimación cargada");
        }
        PerfilDesarrollador perfil = buscarPerfilActivo(dto.perfilEstimadoId());
        Usuario creador = buscarAdminActivo(usuarioAutenticado().getId());
        BigDecimal costo = calcularCosto(dto.horasEstimadas(), perfil);

        Estimacion estimacion = EstimacionMapper.toEntity(dto, tarea, perfil, creador, costo);
        estimacion = estimacionRepository.save(estimacion);
        return EstimacionMapper.toResponseDTO(estimacion);
    }

    @Transactional(readOnly = true)
    public EstimacionResponseDTO obtenerPorTarea(Long tareaId) {
        return EstimacionMapper.toResponseDTO(buscarPorTareaOLanzar(tareaId));
    }

    public EstimacionResponseDTO actualizar(Long tareaId, EstimacionRequestDTO dto) {
        Estimacion estimacion = buscarPorTareaOLanzar(tareaId);
        buscarTareaConProyectoAbierto(tareaId);
        validarSinHorasCargadas(tareaId);
        PerfilDesarrollador perfil = buscarPerfilActivo(dto.perfilEstimadoId());
        BigDecimal costo = calcularCosto(dto.horasEstimadas(), perfil);

        EstimacionMapper.updateEntity(estimacion, dto, perfil, costo);
        return EstimacionMapper.toResponseDTO(estimacion);
    }

    public void eliminar(Long tareaId) {
        Estimacion estimacion = buscarPorTareaOLanzar(tareaId);
        validarSinHorasCargadas(tareaId);
        estimacionRepository.delete(estimacion);
    }

    private Usuario usuarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado: " + email));
    }

    private BigDecimal calcularCosto(BigDecimal horas, PerfilDesarrollador perfil) {
        return horas.multiply(perfil.getCostoHora()).setScale(2, RoundingMode.HALF_UP);
    }

    private Estimacion buscarPorTareaOLanzar(Long tareaId) {
        return estimacionRepository.findByTareaId(tareaId)
                .orElseThrow(() -> new ResourceNotFoundException("La tarea " + tareaId + " no tiene estimación"));
    }

    private Tarea buscarTareaConProyectoAbierto(Long tareaId) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada: id=" + tareaId));
        EstadoProyecto estado = tarea.getModulo().getProyecto().getEstado();
        if (estado == EstadoProyecto.CANCELADO || estado == EstadoProyecto.FINALIZADO) {
            throw new BusinessRuleException("El proyecto de la tarea está " + estado + " y no admite cambios");
        }
        return tarea;
    }

    private PerfilDesarrollador buscarPerfilActivo(Long perfilId) {
        PerfilDesarrollador perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil no encontrado: id=" + perfilId));
        if (!perfil.isActivo()) {
            throw new BusinessRuleException("El perfil " + perfil.getNombre() + " está dado de baja");
        }
        return perfil;
    }

    private Usuario buscarAdminActivo(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: id=" + usuarioId));
        if (!usuario.isActivo()) {
            throw new BusinessRuleException("El usuario " + usuarioId + " está dado de baja");
        }
        if (usuario.getRol() != RolUsuario.ADMIN) {
            throw new BusinessRuleException("Solo un administrador puede cargar estimaciones");
        }
        return usuario;
    }

    private void validarSinHorasCargadas(Long tareaId) {
        if (registroTiempoRepository.existsByTareaId(tareaId)) {
            throw new BusinessRuleException("No se puede modificar la estimación porque la tarea ya tiene horas reales cargadas");
        }
    }
}