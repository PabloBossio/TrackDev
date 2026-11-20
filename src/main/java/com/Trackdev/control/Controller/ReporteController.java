package com.Trackdev.control.Controller;

import com.Trackdev.control.Dto.RentabilidadResponseDTO;
import com.Trackdev.control.Service.RentabilidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final RentabilidadService rentabilidadService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/proyectos/{proyectoId}/rentabilidad")
    public RentabilidadResponseDTO rentabilidadProyecto(@PathVariable Long proyectoId) {
        return rentabilidadService.calcular(proyectoId);
    }
}