package com.Trackdev.control.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "razon_social", nullable = false, length = 150)
    private String razonSocial;

    @Column(length = 20, unique = true)
    private String cuit;

    @Column(name = "contacto_nombre", length = 120)
    private String contactoNombre;

    @Email
    @Column(name = "contacto_email", length = 150)
    private String contactoEmail;

    @Column(name = "contacto_telefono", length = 30)
    private String contactoTelefono;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}