package com.Trackdev.control.Controller;

import com.Trackdev.control.Dto.ModuloRequestDTO;
import com.Trackdev.control.Dto.ModuloResponseDTO;
import com.Trackdev.control.Service.ModuloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
@RequiredArgsConstructor
public class ModuloController {

    private final ModuloService moduloService;

    @PostMapping
    public ResponseEntity<ModuloResponseDTO> crear(@Valid @RequestBody ModuloRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moduloService.crear(dto));
    }

    @GetMapping
    public List<ModuloResponseDTO> listarPorProyecto(@RequestParam Long proyectoId) {
        return moduloService.listarPorProyecto(proyectoId);
    }

    @GetMapping("/{id}")
    public ModuloResponseDTO obtenerPorId(@PathVariable Long id) {
        return moduloService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public ModuloResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ModuloRequestDTO dto) {
        return moduloService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        moduloService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}