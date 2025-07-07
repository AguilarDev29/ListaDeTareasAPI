package com.example.ListaTareas.models.usuario.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DtoCredencialesUsuario(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 6, max = 50, message = "El usuario debe tener entre {min} y {max} caracteres")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre {min} y {max} caracteres")
        String password
) {
}
