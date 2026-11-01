package com.Trackdev.control.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "estimacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estimacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tarea_id", nullable = false, unique = true)
    private Tarea tarea;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfil_estimado_id", nullable = false)
    private PerfilDesarrollador perfilEstimado;

    @NotNull
    @Positive
    @Column(name = "horas_estimadas", nullable = false, precision = 8, scale = 2)
    private BigDecimal horasEstimadas;

    @NotNull
    @Positive
    @Column(name = "costo_estimado", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoEstimado;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creado_por", nullable = false)
    private Usuario creadoPor;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}