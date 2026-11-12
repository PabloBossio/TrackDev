package com.Trackdev.control.Service;

import com.Trackdev.control.Dto.ModuloRequestDTO;
import com.Trackdev.control.Dto.ModuloResponseDTO;
import com.Trackdev.control.Entity.Enums.EstadoProyecto;
import com.Trackdev.control.Entity.Modulo;
import com.Trackdev.control.Entity.Proyecto;
import com.Trackdev.control.Exception.BusinessRuleException;
import com.Trackdev.control.Exception.ResourceNotFoundException;
import com.Trackdev.control.Mapper.ModuloMapper;
import com.Trackdev.control.Repository.ModuloRepository;
import com.Trackdev.control.Repository.ProyectoRepository;
import com.Trackdev.control.Repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ModuloService {

    private final ModuloRepository moduloRepository;
    private final ProyectoRepository proyectoRepository;
    private final TareaRepository tareaRepository;

    public ModuloResponseDTO crear(ModuloRequestDTO dto) {
        Proyecto proyecto = buscarProyectoAbierto(dto.proyectoId());
        if (moduloRepository.existsByProyectoIdAndNombre(dto.proyectoId(), dto.nombre())) {
            throw new BusinessRuleException("Ya existe un módulo llamado '" + dto.nombre() + "' en este proyecto");
        }
        Modulo modulo = ModuloMapper.toEntity(dto, proyecto);
        modulo = moduloRepository.save(modulo);
        return ModuloMapper.toResponseDTO(modulo);
    }

    @Transactional(readOnly = true)
    public List<ModuloResponseDTO> listarPorProyecto(Long proyectoId) {
        if (!proyectoRepository.existsById(proyectoId)) {
            throw new ResourceNotFoundException("Proyecto no encontrado: id=" + proyectoId);
        }
        return moduloRepository.findByProyectoId(proyectoId).stream()
                .map(ModuloMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ModuloResponseDTO obtenerPorId(Long id) {
        return ModuloMapper.toResponseDTO(buscarOLanzar(id));
    }

    public ModuloResponseDTO actualizar(Long id, ModuloRequestDTO dto) {
        Modulo modulo = buscarOLanzar(id);
        Proyecto proyecto = buscarProyectoAbierto(dto.proyectoId());
        if (moduloRepository.existsByProyectoIdAndNombreAndIdNot(dto.proyectoId(), dto.nombre(), id)) {
            throw new BusinessRuleException("Ya existe otro módulo llamado '" + dto.nombre() + "' en este proyecto");
        }
        ModuloMapper.updateEntity(modulo, dto, proyecto);
        return ModuloMapper.toResponseDTO(modulo);
    }

    public void eliminar(Long id) {
        Modulo modulo = buscarOLanzar(id);
        if (tareaRepository.existsByModuloId(id)) {
            throw new BusinessRuleException("No se puede eliminar el módulo porque tiene tareas asociadas");
        }
        moduloRepository.delete(modulo);
    }

    private Modulo buscarOLanzar(Long id) {
        return moduloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo no encontrado: id=" + id));
    }

    private Proyecto buscarProyectoAbierto(Long proyectoId) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: id=" + proyectoId));
        if (proyecto.getEstado() == EstadoProyecto.CANCELADO || proyecto.getEstado() == EstadoProyecto.FINALIZADO) {
            throw new BusinessRuleException("El proyecto " + proyectoId + " está " + proyecto.getEstado() + " y no admite cambios");
        }
        return proyecto;
    }
}