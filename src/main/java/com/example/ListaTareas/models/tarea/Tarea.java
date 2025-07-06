package com.example.ListaTareas.models.tarea;

import com.example.ListaTareas.models.tarea.dto.DtoCrearTarea;
import com.example.ListaTareas.models.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@Entity
@Table(name = "tareas")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String titulo;
    @NotBlank
    private String descripcion;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Estado estado;
    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;
    @Column(name = "fecha_finalizacion")
    private LocalDate fechaFinalizacion = null;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Tarea(DtoCrearTarea tarea) {
        this.titulo = tarea.titulo();
        this.descripcion = tarea.descripcion();
    }


    @PrePersist
    protected void onCreate(){
        this.fechaCreacion = LocalDate.now();
        this.estado = Estado.PENDIENTE;
    }

    public enum Estado {
        PENDIENTE,
        EN_PROGRESO,
        COMPLETADO
    }
}
