package com.Trackdev.control.Controller;

import com.Trackdev.control.Dto.TareaRequestDTO;
import com.Trackdev.control.Dto.TareaResponseDTO;
import com.Trackdev.control.Service.TareaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TareaResponseDTO> crear(@Valid @RequestBody TareaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaService.crear(dto));
    }

    @GetMapping
    public List<TareaResponseDTO> listarPorModulo(@RequestParam Long moduloId) {
        return tareaService.listarPorModulo(moduloId);
    }

    @GetMapping("/{id}")
    public TareaResponseDTO obtenerPorId(@PathVariable Long id) {
        return tareaService.obtenerPorId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public TareaResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody TareaRequestDTO dto) {
        return tareaService.actualizar(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tareaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}