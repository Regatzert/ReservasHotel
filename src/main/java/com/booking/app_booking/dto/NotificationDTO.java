package com.booking.app_booking.dto;

import java.time.LocalDateTime;

import com.booking.app_booking.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDTO {

    private Long id;
    @NotBlank(message = "Titulo del mensaje requerido")
    private String subject;
    @NotBlank(message = "Correo electrónico del destinatario requerido")
    private String recipient; // Correo electrónico del destinatario de la notificación
    private String message; 
    private String bookingReference; // Opcional, para asociar la notificación a una reserva específica
    private NotificationType type; // Enum para diferenciar tipos de notificaciones (reserva, pago, etc.)
    private LocalDateTime createdAt;

}
