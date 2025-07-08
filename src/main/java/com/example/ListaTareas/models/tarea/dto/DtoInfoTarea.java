package com.example.ListaTareas.models.tarea.dto;
import com.example.ListaTareas.models.tarea.Tarea;
import java.time.LocalDateTime;

public record DtoInfoTarea(
        Long id,
        String titulo,
        String descripcion,
        Tarea.Estado estado,
        String usuario,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaInicio,
        LocalDateTime fechaFinalizacion
) {
    public DtoInfoTarea(Tarea tarea) {
        this(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getEstado(),
                tarea.getUsuario().getUsername(),
                tarea.getFechaCreacion(),
                tarea.getFechaInicio(),
                tarea.getFechaFinalizacion()
        );
    }
}
