package com.example.ListaTareas.service;

import com.example.ListaTareas.model.tarea.TareaEntity;
import com.example.ListaTareas.model.tarea.dto.DtoInfoTarea;
import com.example.ListaTareas.repository.TareaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TareaService {
    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public Page<TareaEntity> findAll(Pageable pageable) {
        return tareaRepository.findAll(pageable);
    }

    public Optional<TareaEntity> getTareaById(Long id){
        return tareaRepository.findById(id);
    }

    public TareaEntity saveTarea(TareaEntity tareaEntity){
        return tareaRepository.save(tareaEntity);
    }

    public void deleteTarea(Long id){
        tareaRepository.deleteById(id);
    }

    public Page<DtoInfoTarea> tareasCompletadas(Pageable pageable){
        return tareaRepository.tareasCompletadas(pageable);
    }

    public Page<DtoInfoTarea> tareasPendientes(Pageable pageable){
        return tareaRepository.tareasPendientes(pageable);
    }
}
