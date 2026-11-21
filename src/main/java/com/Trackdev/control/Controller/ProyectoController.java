package com.Trackdev.control.Controller;

import com.Trackdev.control.Dto.ProyectoRequestDTO;
import com.Trackdev.control.Dto.ProyectoResponseDTO;
import com.Trackdev.control.Service.ProyectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProyectoResponseDTO> crear(@Valid @RequestBody ProyectoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoService.crear(dto));
    }

    @GetMapping
    public List<ProyectoResponseDTO> listar(@RequestParam(required = false) Long clienteId) {
        if (clienteId != null) {
            return proyectoService.listarPorCliente(clienteId);
        }
        return proyectoService.listar();
    }

    @GetMapping("/{id}")
    public ProyectoResponseDTO obtenerPorId(@PathVariable Long id) {
        return proyectoService.obtenerPorId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProyectoResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ProyectoRequestDTO dto) {
        return proyectoService.actualizar(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        proyectoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}