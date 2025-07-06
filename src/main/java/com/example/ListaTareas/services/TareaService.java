package com.example.ListaTareas.services;

import com.example.ListaTareas.models.tarea.Tarea;
import com.example.ListaTareas.models.tarea.dto.DtoCrearTarea;
import com.example.ListaTareas.models.tarea.dto.DtoEditarTarea;
import com.example.ListaTareas.models.tarea.dto.DtoInfoTarea;
import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.repositories.TareaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TareaService {
    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public Page<Tarea> getAll(Pageable pageable) {
        return tareaRepository.findAll(pageable);
    }

    public Page<Tarea> getTareasByUsuario(@AuthenticationPrincipal Usuario usuario, Pageable pageable){
        return tareaRepository.findByUsuarioAndEstado(usuario,pageable, Tarea.Estado.PENDIENTE);
    }

    public Tarea getTareaByIdAndUsuario(Long id, Usuario usuario){
        return tareaRepository.findByIdAndUsuario(id, usuario);
    }


    public Tarea saveTarea(DtoCrearTarea tarea, Usuario usuario){
        var nuevaTarea = new Tarea(tarea);
        nuevaTarea.setUsuario(usuario);
        return tareaRepository.save(nuevaTarea);
    }

    public Tarea updateTarea(DtoEditarTarea tarea, Long id, Usuario usuario){
        var tareaToUpdate = verificarTarea(id, usuario);

        if(tareaToUpdate != null){
            if(!tarea.titulo().isBlank()) tareaToUpdate.setTitulo(tarea.titulo());
            if(!tarea.titulo().isBlank()) tareaToUpdate.setDescripcion(tarea.descripcion());
            return tareaRepository.save(tareaToUpdate);
        }
        return null;
    }

    public void completarTarea(Long id){
        var tarea = tareaRepository.findById(id);

        if(tarea.isPresent()){
            tarea.get().setEstado(Tarea.Estado.COMPLETADO);
            tarea.get().setFechaFinalizacion(LocalDate.now());
            tareaRepository.save(tarea.get());
        }
    }

    public void deleteTarea(Long id){
        tareaRepository.deleteById(id);
    }

    public Page<DtoInfoTarea> tareasCompletadas(Pageable pageable){
        return tareaRepository.tareasCompletadas(pageable).map(DtoInfoTarea::new);
    }

    public Page<DtoInfoTarea> tareasPendientes(Pageable pageable){
        return tareaRepository.tareasPendientes(pageable).map(DtoInfoTarea::new);
    }

    private Tarea verificarTarea(Long tareaId, Usuario usuario){
        return tareaRepository.findById(tareaId).filter(t -> t.getUsuario().getId().equals(usuario.getId())).orElseThrow(() -> new EntityNotFoundException("Tarea no encontrada o no pertenece al usuario"));
    }
}
