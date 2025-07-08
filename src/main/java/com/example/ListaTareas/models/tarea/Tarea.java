package com.example.ListaTareas.models.tarea;
import com.example.ListaTareas.models.tarea.dto.DtoCrearTarea;
import com.example.ListaTareas.models.tarea.dto.DtoEditarTarea;
import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.utils.DateTimeUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

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
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long id;
    @NotBlank
    private String titulo;
    @NotBlank
    private String descripcion;
    @Enumerated(EnumType.STRING)
    @NotNull
    private Estado estado;
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "usuario_id",
            referencedColumnName = "id",
            columnDefinition = "BIGINT UNSIGNED"
    )
    private Usuario usuario;

    public Tarea(DtoCrearTarea tarea) {
        titulo = tarea.titulo();
        descripcion = tarea.descripcion();
    }

    public Tarea(DtoEditarTarea datosEditarTarea) {
        this.titulo = datosEditarTarea.titulo();
        this.descripcion = datosEditarTarea.descripcion();
    }

    @PrePersist
    protected void onCreate(){
        this.fechaCreacion = DateTimeUtils
                .parseLocalDateTime(LocalDateTime.now().toString());
        this.fechaInicio = null;
        this.fechaFinalizacion = null;
        this.estado = Estado.PENDIENTE;
    }

    public enum Estado {
        PENDIENTE,
        EN_PROGRESO,
        COMPLETADO
    }
}
