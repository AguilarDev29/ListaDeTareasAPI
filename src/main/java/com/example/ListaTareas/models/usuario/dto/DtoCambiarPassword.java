package com.example.ListaTareas.models.usuario.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DtoCambiarPassword(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre {min} y {max} caracteres")
        String newPassword) {
}
