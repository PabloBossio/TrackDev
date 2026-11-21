package com.Trackdev.control.Service;

import com.Trackdev.control.Dto.AlertaDTO;
import com.Trackdev.control.Dto.DesvioResponseDTO;
import com.Trackdev.control.Entity.Estimacion;
import com.Trackdev.control.Entity.Proyecto;
import com.Trackdev.control.Entity.Usuario;
import com.Trackdev.control.Exception.ResourceNotFoundException;
import com.Trackdev.control.Repository.EstimacionRepository;
import com.Trackdev.control.Repository.ProyectoRepository;
import com.Trackdev.control.Repository.RegistroTiempoRepository;
import com.Trackdev.control.Repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlertaService {

    private static final BigDecimal UMBRAL_ADVERTENCIA = BigDecimal.valueOf(80);
    private static final BigDecimal UMBRAL_CRITICO = BigDecimal.valueOf(100);

    private final DesvioService desvioService;
    private final EstimacionRepository estimacionRepository;
    private final RegistroTiempoRepository registroTiempoRepository;
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<AlertaDTO> listarAlertas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean esAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Usuario autenticado = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + auth.getName()));

        return esAdmin ? listarTodas() : listarPropias(autenticado);
    }

    private List<AlertaDTO> listarTodas() {
        List<AlertaDTO> alertas = new ArrayList<>();

        for (Estimacion estimacion : estimacionRepository.findAll()) {
            Long tareaId = estimacion.getTarea().getId();
            String nombreTarea = estimacion.getTarea().getNombre();
            DesvioResponseDTO desvio = desvioService.calcularPorTarea(tareaId);
            agregarSiCorresponde(alertas, "TAREA", tareaId, nombreTarea, "HORAS", desvio.porcentajeConsumidoHoras());
            agregarSiCorresponde(alertas, "TAREA", tareaId, nombreTarea, "COSTO", desvio.porcentajeConsumidoCosto());
        }

        for (Proyecto proyecto : proyectoRepository.findAll()) {
            DesvioResponseDTO desvio = desvioService.calcularPorProyecto(proyecto.getId());
            agregarSiCorresponde(alertas, "PROYECTO", proyecto.getId(), proyecto.getNombre(), "HORAS", desvio.porcentajeConsumidoHoras());
            agregarSiCorresponde(alertas, "PROYECTO", proyecto.getId(), proyecto.getNombre(), "COSTO", desvio.porcentajeConsumidoCosto());
        }

        return alertas;
    }

    private List<AlertaDTO> listarPropias(Usuario operador) {
        List<AlertaDTO> alertas = new ArrayList<>();

        Set<Long> tareaIds = registroTiempoRepository.findByUsuarioId(operador.getId()).stream()
                .map(r -> r.getTarea().getId())
                .collect(Collectors.toSet());

        for (Long tareaId : tareaIds) {
            estimacionRepository.findByTareaId(tareaId).ifPresent(estimacion -> {
                DesvioResponseDTO desvio = desvioService.calcularPorTarea(tareaId);
                agregarSiCorresponde(alertas, "TAREA", tareaId, estimacion.getTarea().getNombre(), "HORAS", desvio.porcentajeConsumidoHoras());
            });
        }

        return alertas;
    }

    private void agregarSiCorresponde(List<AlertaDTO> alertas, String tipo, Long id, String nombre,
                                      String metrica, BigDecimal porcentaje) {
        if (porcentaje == null || porcentaje.compareTo(UMBRAL_ADVERTENCIA) < 0) {
            return;
        }
        String nivel = porcentaje.compareTo(UMBRAL_CRITICO) >= 0 ? "CRITICO" : "ADVERTENCIA";
        alertas.add(new AlertaDTO(tipo, id, nombre, metrica, porcentaje, nivel));
    }
}