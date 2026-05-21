package com.booking.app_booking.entity;

import java.time.LocalDateTime;

import com.booking.app_booking.enums.NotificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "notifications")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    
    @NotBlank(message = "Correo electrónico del destinatario requerido")
    private String recipient; // Correo electrónico del destinatario de la notificación

    private String body; 

    private String bookingReference; // Opcional, para asociar la notificación a una reserva específica

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType; // Enum para diferenciar tipos de notificaciones (reserva, pago, etc.)

    private final LocalDateTime createdAt = LocalDateTime.now();
}
