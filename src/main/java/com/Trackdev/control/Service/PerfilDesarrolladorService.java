package com.Trackdev.control.Service;

import com.Trackdev.control.Entity.PerfilDesarrollador;
import com.Trackdev.control.Dto.PerfilDesarrolladorRequestDTO;
import com.Trackdev.control.Dto.PerfilDesarrolladorResponseDTO;
import com.Trackdev.control.Mapper.PerfilDesarrolladorMapper;
import com.Trackdev.control.Repository.PerfilDesarrolladorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PerfilDesarrolladorService {

    private final PerfilDesarrolladorRepository perfilRepository;

    public PerfilDesarrolladorResponseDTO crear(PerfilDesarrolladorRequestDTO dto) {
        PerfilDesarrollador perfil = PerfilDesarrolladorMapper.toEntity(dto);
        perfil = perfilRepository.save(perfil);
        return PerfilDesarrolladorMapper.toResponseDTO(perfil);
    }

    @Transactional(readOnly = true)
    public List<PerfilDesarrolladorResponseDTO> listar() {
        return perfilRepository.findAll().stream()
                .map(PerfilDesarrolladorMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PerfilDesarrolladorResponseDTO obtenerPorId(Long id) {
        return PerfilDesarrolladorMapper.toResponseDTO(buscarOLanzar(id));
    }

    public PerfilDesarrolladorResponseDTO actualizar(Long id, PerfilDesarrolladorRequestDTO dto) {
        PerfilDesarrollador perfil = buscarOLanzar(id);
        PerfilDesarrolladorMapper.updateEntity(perfil, dto);
        return PerfilDesarrolladorMapper.toResponseDTO(perfil);
    }

    public void desactivar(Long id) {
        PerfilDesarrollador perfil = buscarOLanzar(id);
        perfil.setActivo(false);
    }

    private PerfilDesarrollador buscarOLanzar(Long id) {
        return perfilRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PerfilDesarrollador no encontrado: id=" + id));
    }
}