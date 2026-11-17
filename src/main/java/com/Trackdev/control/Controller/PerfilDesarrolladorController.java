package com.Trackdev.control.Controller;

import com.Trackdev.control.Dto.PerfilDesarrolladorRequestDTO;
import com.Trackdev.control.Dto.PerfilDesarrolladorResponseDTO;
import com.Trackdev.control.Service.PerfilDesarrolladorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfiles")
@RequiredArgsConstructor
public class PerfilDesarrolladorController {

    private final PerfilDesarrolladorService perfilService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PerfilDesarrolladorResponseDTO> crear(@Valid @RequestBody PerfilDesarrolladorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(perfilService.crear(dto));
    }

    @GetMapping
    public List<PerfilDesarrolladorResponseDTO> listar() {
        return perfilService.listar();
    }

    @GetMapping("/{id}")
    public PerfilDesarrolladorResponseDTO obtenerPorId(@PathVariable Long id) {
        return perfilService.obtenerPorId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public PerfilDesarrolladorResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody PerfilDesarrolladorRequestDTO dto) {
        return perfilService.actualizar(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        perfilService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}