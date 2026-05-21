package com.booking.app_booking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "Correo electrónico requerido")
    private String email;
    @NotBlank(message = "Contraseña requerida")
    private String password;

}
