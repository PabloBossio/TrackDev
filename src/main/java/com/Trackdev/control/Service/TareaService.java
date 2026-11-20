package com.Trackdev.control.Service;

import com.Trackdev.control.Dto.TareaRequestDTO;
import com.Trackdev.control.Dto.TareaResponseDTO;
import com.Trackdev.control.Entity.Enums.EstadoProyecto;
import com.Trackdev.control.Entity.Modulo;
import com.Trackdev.control.Entity.Tarea;
import com.Trackdev.control.Exception.BusinessRuleException;
import com.Trackdev.control.Exception.ResourceNotFoundException;
import com.Trackdev.control.Mapper.TareaMapper;
import com.Trackdev.control.Repository.EstimacionRepository;
import com.Trackdev.control.Repository.ModuloRepository;
import com.Trackdev.control.Repository.RegistroTiempoRepository;
import com.Trackdev.control.Repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TareaService {

    private final TareaRepository tareaRepository;
    private final ModuloRepository moduloRepository;
    private final EstimacionRepository estimacionRepository;
    private final RegistroTiempoRepository registroTiempoRepository;

    public TareaResponseDTO crear(TareaRequestDTO dto) {
        Modulo modulo = buscarModuloConProyectoAbierto(dto.moduloId());
        Tarea tarea = TareaMapper.toEntity(dto, modulo);
        tarea = tareaRepository.save(tarea);
        return TareaMapper.toResponseDTO(tarea);
    }

    @Transactional(readOnly = true)
    public List<TareaResponseDTO> listarPorModulo(Long moduloId) {
        if (!moduloRepository.existsById(moduloId)) {
            throw new ResourceNotFoundException("Módulo no encontrado: id=" + moduloId);
        }
        return tareaRepository.findByModuloId(moduloId).stream()
                .map(TareaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public TareaResponseDTO obtenerPorId(Long id) {
        return TareaMapper.toResponseDTO(buscarOLanzar(id));
    }

    public TareaResponseDTO actualizar(Long id, TareaRequestDTO dto) {
        Tarea tarea = buscarOLanzar(id);
        Modulo modulo = buscarModuloConProyectoAbierto(dto.moduloId());
        TareaMapper.updateEntity(tarea, dto, modulo);
        return TareaMapper.toResponseDTO(tarea);
    }

    public void eliminar(Long id) {
        Tarea tarea = buscarOLanzar(id);
        if (estimacionRepository.existsByTareaId(id) || registroTiempoRepository.existsByTareaId(id)) {
            throw new BusinessRuleException("No se puede eliminar la tarea porque tiene estimación u horas cargadas");
        }
        tareaRepository.delete(tarea);
    }

    private Tarea buscarOLanzar(Long id) {
        return tareaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada: id=" + id));
    }

    private Modulo buscarModuloConProyectoAbierto(Long moduloId) {
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo no encontrado: id=" + moduloId));
        EstadoProyecto estado = modulo.getProyecto().getEstado();
        if (estado == EstadoProyecto.CANCELADO || estado == EstadoProyecto.FINALIZADO) {
            throw new BusinessRuleException("El proyecto del módulo está " + estado + " y no admite cambios");
        }
        return modulo;
    }

    @Transactional(readOnly = true)
    public List<TareaResponseDTO> listarDisponiblesParaCargar() {
        return tareaRepository.findDisponiblesParaCargar().stream()
                .map(TareaMapper::toResponseDTO)
                .toList();
    }
}