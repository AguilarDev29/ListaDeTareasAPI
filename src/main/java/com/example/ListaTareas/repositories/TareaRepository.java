package com.example.ListaTareas.repositories;

import com.example.ListaTareas.models.tarea.Tarea;
import com.example.ListaTareas.models.tarea.dto.DtoInfoTarea;
import com.example.ListaTareas.models.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TareaRepository extends JpaRepository<Tarea, Long> {


    @Query("SELECT t FROM Tarea t WHERE t.estado = 'COMPLETADO'")
    Page<Tarea> tareasCompletadas(Pageable pageable);

    Optional<Tarea> findByIdAndUsuario(Long id, Usuario usuario);

    Page<Tarea> findByUsuarioAndEstado(Usuario usuario, Pageable pageable, Tarea.Estado estado);

}
