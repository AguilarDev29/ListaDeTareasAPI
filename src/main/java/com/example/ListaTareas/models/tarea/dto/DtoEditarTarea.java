package com.example.ListaTareas.models.tarea.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DtoEditarTarea(
        @Size(max = 50, message = "El titulo no debe superar los 50 caracteres")
        String titulo,
        @Size(max = 255, message = "La descripcion no debe superar los 255 caracteres")
        String descripcion
) {
}
