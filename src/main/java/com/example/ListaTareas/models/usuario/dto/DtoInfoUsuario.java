package com.example.ListaTareas.models.usuario.dto;
import com.example.ListaTareas.models.tarea.dto.DtoInfoTarea;
import com.example.ListaTareas.models.usuario.Usuario;
import java.util.List;

public record DtoInfoUsuario(
        Long id,
        String username,
        String password,
        String roles,
        List<DtoInfoTarea> tareas
) {
    public DtoInfoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getUsername(),
                usuario.getPassword(), String.join(", ", usuario.getRoles()),
                usuario.getTareas().stream().map(DtoInfoTarea::new).toList());
    }
}
