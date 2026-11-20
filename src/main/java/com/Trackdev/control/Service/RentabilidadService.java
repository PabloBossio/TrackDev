package com.Trackdev.control.Service;

import com.Trackdev.control.Dto.RentabilidadResponseDTO;
import com.Trackdev.control.Entity.Proyecto;
import com.Trackdev.control.Exception.ResourceNotFoundException;
import com.Trackdev.control.Repository.EstimacionRepository;
import com.Trackdev.control.Repository.ProyectoRepository;
import com.Trackdev.control.Repository.RegistroTiempoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RentabilidadService {

    private final ProyectoRepository proyectoRepository;
    private final EstimacionRepository estimacionRepository;
    private final RegistroTiempoRepository registroTiempoRepository;

    public RentabilidadResponseDTO calcular(Long proyectoId) {
        Proyecto proyecto = proyectoRepository.findById(proyectoId)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: id=" + proyectoId));

        BigDecimal presupuesto = proyecto.getPresupuestoVenta() != null
                ? proyecto.getPresupuestoVenta()
                : BigDecimal.ZERO;

        BigDecimal horasEstimadas = estimacionRepository.sumHorasEstimadasPorProyecto(proyectoId);
        BigDecimal costoEstimado = estimacionRepository.sumCostoEstimadoPorProyecto(proyectoId);
        BigDecimal horasReales = registroTiempoRepository.sumHorasTrabajadasPorProyecto(proyectoId);
        BigDecimal costoReal = registroTiempoRepository.sumCostoRealPorProyecto(proyectoId);

        BigDecimal rentabilidadEstimada = presupuesto.subtract(costoEstimado);
        BigDecimal rentabilidadReal = presupuesto.subtract(costoReal);

        return new RentabilidadResponseDTO(
                proyecto.getId(),
                proyecto.getNombre(),
                presupuesto,
                horasEstimadas,
                costoEstimado,
                rentabilidadEstimada,
                calcularMargen(rentabilidadEstimada, presupuesto),
                horasReales,
                costoReal,
                rentabilidadReal,
                calcularMargen(rentabilidadReal, presupuesto)
        );
    }

    private BigDecimal calcularMargen(BigDecimal rentabilidad, BigDecimal presupuesto) {
        if (presupuesto.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return rentabilidad
                .divide(presupuesto, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}