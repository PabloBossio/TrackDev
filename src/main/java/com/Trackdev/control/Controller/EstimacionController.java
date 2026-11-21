package com.Trackdev.control.Controller;

import com.Trackdev.control.Dto.EstimacionRequestDTO;
import com.Trackdev.control.Dto.EstimacionResponseDTO;
import com.Trackdev.control.Service.EstimacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/tareas/{tareaId}/estimacion")
@RequiredArgsConstructor
public class EstimacionController {

    private final EstimacionService estimacionService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EstimacionResponseDTO> crear(@PathVariable Long tareaId,
                                                       @Valid @RequestBody EstimacionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estimacionService.crear(tareaId, dto));
    }

    @GetMapping
    public EstimacionResponseDTO obtener(@PathVariable Long tareaId) {
        return estimacionService.obtenerPorTarea(tareaId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public EstimacionResponseDTO actualizar(@PathVariable Long tareaId,
                                            @Valid @RequestBody EstimacionRequestDTO dto) {
        return estimacionService.actualizar(tareaId, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Void> eliminar(@PathVariable Long tareaId) {
        estimacionService.eliminar(tareaId);
        return ResponseEntity.noContent().build();
    }
}