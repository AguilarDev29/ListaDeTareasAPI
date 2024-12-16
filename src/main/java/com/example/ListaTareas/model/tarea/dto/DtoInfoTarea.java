package com.example.ListaTareas.model.tarea.dto;

import com.example.ListaTareas.model.tarea.Estado;
import com.example.ListaTareas.model.tarea.TareaEntity;

import java.time.LocalDate;

public record DtoInfoTarea(
        String titulo,
        String descripcion,
        Estado estado,
        LocalDate fechaCreacion,
        LocalDate fechaFinalizacion
) {
    public DtoInfoTarea(TareaEntity tarea) {
        this(
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getEstado(),
                tarea.getFechaCreacion(),
                tarea.getFechaFinalizacion()
        );
    }
}
