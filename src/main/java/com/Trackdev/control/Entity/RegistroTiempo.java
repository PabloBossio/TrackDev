package com.Trackdev.control.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_tiempo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroTiempo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tarea_id", nullable = false)
    private Tarea tarea;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull
    @Positive
    @DecimalMax(value = "24.00")
    @Column(name = "horas_trabajadas", nullable = false, precision = 5, scale = 2)
    private BigDecimal horasTrabajadas;

    @NotNull
    @Positive
    @Column(name = "costo_hora_aplicado", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoHoraAplicado;

    @NotNull
    @Positive
    @Column(name = "costo_real", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoReal;

    @Size(max = 300)
    @Column(length = 300)
    private String descripcion;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}