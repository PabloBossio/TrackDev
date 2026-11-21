package com.Trackdev.control.Controller;

import com.Trackdev.control.Dto.DesvioResponseDTO;
import com.Trackdev.control.Dto.RentabilidadResponseDTO;
import com.Trackdev.control.Service.DesvioService;
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
    private final DesvioService desvioService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/proyectos/{proyectoId}/rentabilidad")
    public RentabilidadResponseDTO rentabilidadProyecto(@PathVariable Long proyectoId) {
        return rentabilidadService.calcular(proyectoId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/tareas/{tareaId}/desvio")
    public DesvioResponseDTO desvioTarea(@PathVariable Long tareaId) {
        return desvioService.calcularPorTarea(tareaId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/modulos/{moduloId}/desvio")
    public DesvioResponseDTO desvioModulo(@PathVariable Long moduloId) {
        return desvioService.calcularPorModulo(moduloId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/proyectos/{proyectoId}/desvio")
    public DesvioResponseDTO desvioProyecto(@PathVariable Long proyectoId) {
        return desvioService.calcularPorProyecto(proyectoId);
    }
}