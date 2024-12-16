package com.example.ListaTareas.controller;

import com.example.ListaTareas.model.tarea.Estado;
import com.example.ListaTareas.model.tarea.TareaEntity;
import com.example.ListaTareas.model.tarea.dto.DtoEditarTarea;
import com.example.ListaTareas.model.tarea.dto.DtoInfoTarea;
import com.example.ListaTareas.service.TareaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/v1/tasks")
public class TareaController {
    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }
    //EndPoints GET
    @GetMapping
    public ResponseEntity<Page<DtoInfoTarea>> getListTarea(@PageableDefault(value = 5) Pageable pageable) {
        return ResponseEntity.ok(tareaService.tareasPendientes(pageable));
    }
    @GetMapping("/completes")
    public ResponseEntity<Page<DtoInfoTarea>>getTareasCompletadas(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(tareaService.tareasCompletadas(pageable));
    }
    @GetMapping("/{id}")
    public ResponseEntity<DtoInfoTarea> getTareaById(@PathVariable Long id){
        if(tareaService.getTareaById(id).isPresent()){
            return ResponseEntity.ok(tareaService.getTareaById(id).map(DtoInfoTarea::new).get());
        }
        return ResponseEntity.notFound().build();
    }
    //EndPoint POST
    @PostMapping
    public ResponseEntity<TareaEntity> createTarea(@RequestBody @Valid TareaEntity tareaEntity){
        tareaService.saveTarea(tareaEntity);
        return new ResponseEntity(tareaEntity, HttpStatus.CREATED);
    }
    //EndPoints PUT
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<TareaEntity> editarTarea(@PathVariable Long id, @RequestBody DtoEditarTarea datosEditarTarea){
        return tareaService.getTareaById(id).map(t -> {
            if(datosEditarTarea.titulo() != null){
                t.setTitulo(datosEditarTarea.titulo());
            }
            if(datosEditarTarea.descripcion() != null){
                t.setDescripcion(datosEditarTarea.descripcion());
            }
            TareaEntity tareaActualizada = tareaService.saveTarea(t);
            return ResponseEntity.ok(tareaActualizada);
        }
        ).orElse(ResponseEntity.notFound().build());
    }
    //Borrado logico
    @PutMapping("/{id}/ok")
    @Transactional
    public ResponseEntity<TareaEntity> completarTarea(@PathVariable Long id, @RequestBody TareaEntity tareaEntity){
        return tareaService.getTareaById(id)
                .map(t -> {
                    t.setEstado(Estado.COMPLETADO);
                    t.setFechaFinalizacion(LocalDate.now());
                    return ResponseEntity.ok(tareaService.saveTarea(t));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    //EndPoint DELETE
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteTarea(@PathVariable Long id){
        if(tareaService.getTareaById(id).isPresent()){
            tareaService.deleteTarea(id);
            return ResponseEntity.noContent().build();
        }
        return  ResponseEntity.notFound().build();
    }
}
