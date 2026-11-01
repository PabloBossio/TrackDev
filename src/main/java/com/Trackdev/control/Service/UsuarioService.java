package com.Trackdev.control.Service;

import com.Trackdev.control.Entity.Enums.RolUsuario;
import com.Trackdev.control.Entity.PerfilDesarrollador;
import com.Trackdev.control.Entity.Usuario;
import com.Trackdev.control.Dto.UsuarioRequestDTO;
import com.Trackdev.control.Dto.UsuarioResponseDTO;
import com.Trackdev.control.Mapper.UsuarioMapper;
import com.Trackdev.control.Repository.PerfilDesarrolladorRepository;
import com.Trackdev.control.Repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilDesarrolladorRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO crear(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalStateException("Ya existe un usuario con ese email");
        }

        PerfilDesarrollador perfil = resolverPerfil(dto.rol(), dto.perfilId());
        String passwordHash = passwordEncoder.encode(dto.password());

        Usuario usuario = UsuarioMapper.toEntity(dto, perfil, passwordHash);
        usuario = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponseDTO(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(Long id) {
        return UsuarioMapper.toResponseDTO(buscarOLanzar(id));
    }

    public UsuarioResponseDTO actualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = buscarOLanzar(id);

        if (!usuario.getEmail().equalsIgnoreCase(dto.email()) && usuarioRepository.existsByEmail(dto.email())) {
            throw new IllegalStateException("Ya existe un usuario con ese email");
        }

        PerfilDesarrollador perfil = resolverPerfil(dto.rol(), dto.perfilId());
        UsuarioMapper.updateEntity(usuario, dto, perfil);
        return UsuarioMapper.toResponseDTO(usuario);
    }

    public void desactivar(Long id) {
        Usuario usuario = buscarOLanzar(id);
        usuario.setActivo(false);
    }

    private PerfilDesarrollador resolverPerfil(RolUsuario rol, Long perfilId) {
        if (rol == RolUsuario.OPERADOR && perfilId == null) {
            throw new IllegalArgumentException("Un usuario OPERADOR debe tener un perfil asignado");
        }
        if (perfilId == null) {
            return null;
        }
        return perfilRepository.findById(perfilId)
                .orElseThrow(() -> new EntityNotFoundException("PerfilDesarrollador no encontrado: id=" + perfilId));
    }

    private Usuario buscarOLanzar(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: id=" + id));
    }
}