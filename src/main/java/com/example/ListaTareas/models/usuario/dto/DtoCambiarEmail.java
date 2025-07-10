package com.example.ListaTareas.models.usuario.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DtoCambiarEmail(
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, max = 50, message = "El usuario debe tener entre {min} y {max} caracteres")
        String newEmail) {
}
