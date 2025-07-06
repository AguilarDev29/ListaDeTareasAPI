package com.example.ListaTareas.models.tarea.dto;

import com.example.ListaTareas.models.tarea.Tarea;

import java.time.LocalDate;

public record DtoInfoTarea(
        String titulo,
        String descripcion,
        Tarea.Estado estado,
        LocalDate fechaCreacion,
        LocalDate fechaFinalizacion
) {
    public DtoInfoTarea(Tarea tarea) {
        this(
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getEstado(),
                tarea.getFechaCreacion(),
                tarea.getFechaFinalizacion()
        );
    }
}
