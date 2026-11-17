package com.Trackdev.control.Controller;

import com.Trackdev.control.Dto.UsuarioRequestDTO;
import com.Trackdev.control.Dto.UsuarioResponseDTO;
import com.Trackdev.control.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UsuarioResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.actualizar(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}