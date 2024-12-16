package com.example.ListaTareas.repository;

import com.example.ListaTareas.model.tarea.TareaEntity;
import com.example.ListaTareas.model.tarea.dto.DtoInfoTarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TareaRepository extends JpaRepository<TareaEntity, Long> {

    Page<TareaEntity> findAll(Pageable pageable);

    @Query("SELECT t FROM TareaEntity t WHERE t.estado = 'COMPLETADO'")
    Page<DtoInfoTarea> tareasCompletadas(Pageable pageable);

    @Query("SELECT t FROM TareaEntity t WHERE t.estado = 'PENDIENTE'")
    Page<DtoInfoTarea> tareasPendientes(Pageable pageable);

}
