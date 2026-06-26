package com.example.msnotification.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Schema(description = "Entidad que representa una notificación del sistema")
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la notificación", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull
    @Schema(description = "ID del usuario destinatario", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @NotBlank
    @Column(length = 1000)
    @Schema(description = "Mensaje de la notificación", example = "Tu pedido ha sido procesado correctamente", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mensaje;

    @NotBlank
    @Schema(description = "Tipo de notificación", example = "PEDIDO", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipo;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Estado de la notificación", example = "PENDIENTE", allowableValues = {"PENDIENTE", "ENVIADO", "FALLIDO"})
    private Estado estado = Estado.PENDIENTE;

    @Schema(description = "Fecha de creación", example = "2025-06-01T10:00:00")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public enum Estado { PENDIENTE, ENVIADO, FALLIDO }
}
