package com.Trackdev.control.Controller;

import com.Trackdev.control.Dto.RegistroTiempoRequestDTO;
import com.Trackdev.control.Dto.RegistroTiempoResponseDTO;
import com.Trackdev.control.Service.RegistroTiempoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/registros-tiempo")
@RequiredArgsConstructor
public class RegistroTiempoController {

    private final RegistroTiempoService registroTiempoService;

    @PostMapping
    public ResponseEntity<RegistroTiempoResponseDTO> crear(@Valid @RequestBody RegistroTiempoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroTiempoService.crear(dto));
    }

    @GetMapping
    public List<RegistroTiempoResponseDTO> listar(@RequestParam(required = false) Long tareaId,
                                                  @RequestParam(required = false) Long usuarioId) {
        if (tareaId != null) {
            return registroTiempoService.listarPorTarea(tareaId);
        }
        if (usuarioId != null) {
            return registroTiempoService.listarPorUsuario(usuarioId);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indicá tareaId o usuarioId");
    }

    @GetMapping("/{id}")
    public RegistroTiempoResponseDTO obtenerPorId(@PathVariable Long id) {
        return registroTiempoService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public RegistroTiempoResponseDTO actualizar(@PathVariable Long id,
                                                @Valid @RequestBody RegistroTiempoRequestDTO dto) {
        return registroTiempoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        registroTiempoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mios")
    public List<RegistroTiempoResponseDTO> listarMisRegistros(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) Long proyectoId) {
        return registroTiempoService.listarMisRegistros(desde, hasta, proyectoId);
    }
}