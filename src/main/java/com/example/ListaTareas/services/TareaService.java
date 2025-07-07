package com.example.ListaTareas.services;

import com.example.ListaTareas.models.tarea.Tarea;
import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.repositories.TareaRepository;
import com.example.ListaTareas.utils.DateTimeUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TareaService {
    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public Page<Tarea> getAll(Pageable pageable) {
        return tareaRepository.findAll(pageable);
    }

    public Tarea getTareaById(Long id){
        return tareaRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Tarea no encontrada"));
    }

    public Page<Tarea> getTareasByUsuario(@AuthenticationPrincipal Usuario usuario,
                                          Pageable pageable){
        return tareaRepository.findByUsuarioAndEstado(usuario,pageable,
                                                    Tarea.Estado.PENDIENTE);
    }

    public Page<Tarea> getTareasCompletadasByUsuario(@AuthenticationPrincipal Usuario usuario,
                                                     Pageable pageable){
        return tareaRepository.findByUsuarioAndEstado(usuario,pageable,
                                                    Tarea.Estado.COMPLETADO);
    }

    public Tarea getTareaByIdAndUsuario(Long id, Usuario usuario){
        return tareaRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() ->
                        new EntityNotFoundException("Tarea no encontrada o no pertenece al usuario"));
    }

    public Tarea saveTarea(Tarea tarea, Usuario usuario){
        tarea.setUsuario(usuario);
        return tareaRepository.save(tarea);
    }

    public Tarea updateTarea(Tarea tarea, Long id, Usuario usuario){
        var tareaToUpdate = verificarTarea(id, usuario);

        if(tareaToUpdate != null){
            if(!tarea.getTitulo().isBlank())
                tareaToUpdate.setTitulo(tarea.getTitulo());
            if(!tarea.getDescripcion().isBlank())
                tareaToUpdate.setDescripcion(tarea.getDescripcion());
            return tareaRepository.save(tareaToUpdate);
        }
        return null;
    }

    public void iniciarTarea(Long id){
        var tarea = tareaRepository.findById(id);

        if(tarea.isPresent()){
            tarea.get().setEstado(Tarea.Estado.EN_PROGRESO);
            tarea.get().setFechaInicio(DateTimeUtils
                    .parseLocalDateTime(LocalDateTime.now().toString()));
            tareaRepository.save(tarea.get());
        }
    }

    public void completarTarea(Long id){
        var tarea = tareaRepository.findById(id);

        if(tarea.isPresent()){
            tarea.get().setEstado(Tarea.Estado.COMPLETADO);
            tarea.get().setFechaFinalizacion(DateTimeUtils
                    .parseLocalDateTime(LocalDateTime.now().toString()));
            tareaRepository.save(tarea.get());
        }
    }

    public void deleteTarea(Long id){
        tareaRepository.deleteById(id);
    }

    public Page<Tarea> tareasCompletadas(Pageable pageable){
        return tareaRepository.tareasCompletadas(pageable);
    }

    private Tarea verificarTarea(Long tareaId, Usuario usuario){
        return tareaRepository.findById(tareaId)
                .filter(t -> t.getUsuario().getId().equals(usuario.getId()))
                .orElseThrow(() ->
                        new EntityNotFoundException("Tarea no encontrada o no pertenece al usuario"));
    }
}
