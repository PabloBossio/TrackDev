package com.Trackdev.control.Service;

import com.Trackdev.control.Dto.ClienteRequestDTO;
import com.Trackdev.control.Dto.ClienteResponseDTO;
import com.Trackdev.control.Entity.Cliente;
import com.Trackdev.control.Exception.BusinessRuleException;
import com.Trackdev.control.Exception.ResourceNotFoundException;
import com.Trackdev.control.Mapper.ClienteMapper;
import com.Trackdev.control.Repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        if (dto.cuit() != null && clienteRepository.existsByCuit(dto.cuit())) {
            throw new BusinessRuleException("Ya existe un cliente con el CUIT " + dto.cuit());
        }
        Cliente cliente = ClienteMapper.toEntity(dto);
        cliente = clienteRepository.save(cliente);
        return ClienteMapper.toResponseDTO(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll().stream()
                .map(ClienteMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerPorId(Long id) {
        return ClienteMapper.toResponseDTO(buscarOLanzar(id));
    }

    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = buscarOLanzar(id);
        if (dto.cuit() != null && clienteRepository.existsByCuitAndIdNot(dto.cuit(), id)) {
            throw new BusinessRuleException("Ya existe otro cliente con el CUIT " + dto.cuit());
        }
        ClienteMapper.updateEntity(cliente, dto);
        return ClienteMapper.toResponseDTO(cliente);
    }

    public void desactivar(Long id) {
        Cliente cliente = buscarOLanzar(id);
        cliente.setActivo(false);
    }

    private Cliente buscarOLanzar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: id=" + id));
    }
}