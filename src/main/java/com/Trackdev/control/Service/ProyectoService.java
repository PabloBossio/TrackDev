package com.Trackdev.control.Service;

import com.Trackdev.control.Dto.ProyectoRequestDTO;
import com.Trackdev.control.Dto.ProyectoResponseDTO;
import com.Trackdev.control.Entity.Cliente;
import com.Trackdev.control.Entity.Enums.EstadoProyecto;
import com.Trackdev.control.Entity.Proyecto;
import com.Trackdev.control.Exception.BusinessRuleException;
import com.Trackdev.control.Exception.ResourceNotFoundException;
import com.Trackdev.control.Mapper.ProyectoMapper;
import com.Trackdev.control.Repository.ClienteRepository;
import com.Trackdev.control.Repository.ProyectoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final ClienteRepository clienteRepository;

    public ProyectoResponseDTO crear(ProyectoRequestDTO dto) {
        validarFechas(dto);
        Cliente cliente = buscarClienteActivo(dto.clienteId());
        Proyecto proyecto = ProyectoMapper.toEntity(dto, cliente);
        proyecto = proyectoRepository.save(proyecto);
        return ProyectoMapper.toResponseDTO(proyecto);
    }

    @Transactional(readOnly = true)
    public List<ProyectoResponseDTO> listar() {
        return proyectoRepository.findAll().stream()
                .map(ProyectoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProyectoResponseDTO> listarPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente no encontrado: id=" + clienteId);
        }
        return proyectoRepository.findByClienteId(clienteId).stream()
                .map(ProyectoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProyectoResponseDTO obtenerPorId(Long id) {
        return ProyectoMapper.toResponseDTO(buscarOLanzar(id));
    }

    public ProyectoResponseDTO actualizar(Long id, ProyectoRequestDTO dto) {
        Proyecto proyecto = buscarOLanzar(id);
        validarFechas(dto);
        Cliente cliente = buscarClienteActivo(dto.clienteId());
        ProyectoMapper.updateEntity(proyecto, dto, cliente);
        return ProyectoMapper.toResponseDTO(proyecto);
    }

    public void cancelar(Long id) {
        Proyecto proyecto = buscarOLanzar(id);
        proyecto.setEstado(EstadoProyecto.CANCELADO);
    }

    private Proyecto buscarOLanzar(Long id) {
        return proyectoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: id=" + id));
    }

    private Cliente buscarClienteActivo(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: id=" + clienteId));
        if (!cliente.isActivo()) {
            throw new BusinessRuleException("El cliente " + clienteId + " está dado de baja");
        }
        return cliente;
    }

    private void validarFechas(ProyectoRequestDTO dto) {
        if (dto.fechaFinEstimada() != null && dto.fechaFinEstimada().isBefore(dto.fechaInicio())) {
            throw new BusinessRuleException("La fecha de fin estimada no puede ser anterior a la fecha de inicio");
        }
    }
}