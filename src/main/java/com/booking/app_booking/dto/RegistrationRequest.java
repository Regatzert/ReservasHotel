package com.booking.app_booking.dto;

import com.booking.app_booking.enums.UserRole;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Nombre requerido")
    private String firstName;
    @NotBlank(message = "Apellido requerido")
    private String lastName;
    @NotBlank(message = "Correo electrónico requerido")
    private String email;
    @NotBlank(message = "Número de teléfono requerido")
    private String phoneNumber;

    private UserRole role; // Permitir que el cliente seleccione su rol (CLIENTE o ADMIN)
    @NotBlank(message = "Contraseña requerida")
    private String password;

}
