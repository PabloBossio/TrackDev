package com.Trackdev.control.Service;

import com.Trackdev.control.Dto.DesvioResponseDTO;
import com.Trackdev.control.Entity.Estimacion;
import com.Trackdev.control.Entity.Modulo;
import com.Trackdev.control.Entity.Proyecto;
import com.Trackdev.control.Entity.RegistroTiempo;
import com.Trackdev.control.Entity.Tarea;
import com.Trackdev.control.Exception.ResourceNotFoundException;
import com.Trackdev.control.Repository.EstimacionRepository;
import com.Trackdev.control.Repository.ModuloRepository;
import com.Trackdev.control.Repository.ProyectoRepository;
import com.Trackdev.control.Repository.RegistroTiempoRepository;
import com.Trackdev.control.Repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DesvioService {

    private final TareaRepository tareaRepository;
    private final ModuloRepository moduloRepository;
    private final ProyectoRepository proyectoRepository;
    private final EstimacionRepository estimacionRepository;
    private final RegistroTiempoRepository registroTiempoRepository;

    public DesvioResponseDTO calcularPorTarea(Long tareaId) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada: id=" + tareaId));

        BigDecimal horasEstimadas = estimacionRepository.findByTareaId(tareaId)
                .map(Estimacion::getHorasEstimadas).orElse(BigDecimal.ZERO);
        BigDecimal costoEstimado = estimacionRepository.findByTareaId(tareaId)
                .map(Estimacion::getCostoEstimado).orElse(BigDecimal.ZERO);

        BigDecimal horasReales = registroTiempoRepository.findByTareaId(tareaId).stream()
                .map(RegistroTiempo::getHorasTrabajadas)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal costoReal = registroTiempoRepository.findByTareaId(tareaId).stream()
                .map(RegistroTiempo::getCostoReal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return construir(tarea.getId(), tarea.getNombre(), horasEstimadas, horasReales, costoEstimado, costoReal);
    }

    public DesvioResponseDTO calcularPorModulo(Long moduloId) {
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new ResourceNotFoundException("Módulo no encontrado: id=" + moduloId));

        BigDecimal horasEstimadas = estimacionRepository.sumHorasEstimadasPorModulo(moduloId);
        BigDecimal costoEstimado = estimacionRepository.sumCostoEstimadoPorModulo(moduloId);
        BigDecimal horasReales = registroTiempoRepository.sumHorasTrabajadasPorModulo(moduloId);
        BigDecimal costoReal = registroTiempoRepository.sumCostoRealPorModulo(moduloId);

        return construir(modulo.getId(), modulo.getNombre(), horasEstimadas, horasReales, costoEstimado, costoReal);
    }

    public DesvioResponseDTO calcularPorProyecto(Long proyectoId) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: id=" + proyectoId));

        BigDecimal horasEstimadas = estimacionRepository.sumHorasEstimadasPorProyecto(proyectoId);
        BigDecimal costoEstimado = estimacionRepository.sumCostoEstimadoPorProyecto(proyectoId);
        BigDecimal horasReales = registroTiempoRepository.sumHorasTrabajadasPorProyecto(proyectoId);
        BigDecimal costoReal = registroTiempoRepository.sumCostoRealPorProyecto(proyectoId);

        return construir(proyecto.getId(), proyecto.getNombre(), horasEstimadas, horasReales, costoEstimado, costoReal);
    }

    private DesvioResponseDTO construir(Long id, String nombre, BigDecimal horasEstimadas, BigDecimal horasReales,
                                        BigDecimal costoEstimado, BigDecimal costoReal) {
        BigDecimal desvioHoras = horasReales.subtract(horasEstimadas);
        BigDecimal desvioCosto = costoReal.subtract(costoEstimado);
        return new DesvioResponseDTO(
                id,
                nombre,
                horasEstimadas,
                horasReales,
                desvioHoras,
                calcularPorcentaje(horasReales, horasEstimadas),
                costoEstimado,
                costoReal,
                desvioCosto,
                calcularPorcentaje(costoReal, costoEstimado)
        );
    }

    private BigDecimal calcularPorcentaje(BigDecimal real, BigDecimal estimado) {
        if (estimado.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return real.divide(estimado, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}