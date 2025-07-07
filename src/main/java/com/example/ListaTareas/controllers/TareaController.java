package com.example.ListaTareas.controllers;

import com.example.ListaTareas.models.tarea.Tarea;
import com.example.ListaTareas.models.tarea.dto.DtoCrearTarea;
import com.example.ListaTareas.models.tarea.dto.DtoEditarTarea;
import com.example.ListaTareas.models.tarea.dto.DtoInfoTarea;
import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.services.TareaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;


@RestController
@RequestMapping("/v1/tasks")
public class TareaController {
    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping("/admin")
    public ResponseEntity<Page<DtoInfoTarea>> getListTareas(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(tareaService.getAll(pageable).map(DtoInfoTarea::new));
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<DtoInfoTarea> getListTareas(@PathVariable Long id) {
        return ResponseEntity.ok(new DtoInfoTarea(tareaService.getTareaById(id)));
    }

    @GetMapping("/admin/completes")
    public ResponseEntity<Page<DtoInfoTarea>>getTareasCompletadas(@PageableDefault(size = 5) Pageable pageable) {
        var tarea = tareaService.tareasCompletadas(pageable);
        return ResponseEntity.ok(tarea.map(DtoInfoTarea::new));
    }

    @GetMapping
    public ResponseEntity<Page<DtoInfoTarea>> getListTareasUsuario(@PageableDefault(size = 5) Pageable pageable,
                                                                    @AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(tareaService.getTareasByUsuario(usuario, pageable).map(DtoInfoTarea::new));
    }

    @GetMapping("/completes")
    public ResponseEntity<Page<DtoInfoTarea>>getTareasCompletadasPorUsuario(@AuthenticationPrincipal Usuario usuario,
                                                                            @PageableDefault(size = 5)
                                                                            Pageable pageable) {
        var tarea = tareaService.getTareasCompletadasByUsuario(usuario, pageable);
        return ResponseEntity.ok(tarea.map(DtoInfoTarea::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DtoInfoTarea> getTareaById(@PathVariable Long id,
                                                     @AuthenticationPrincipal Usuario usuario){
        try{
            var tarea = tareaService.getTareaByIdAndUsuario(id, usuario);
            return ResponseEntity.ok(new DtoInfoTarea(tarea));
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<DtoInfoTarea> createTarea(@RequestBody @Valid DtoCrearTarea tarea,
                                                    @AuthenticationPrincipal Usuario usuario){
        var tareaCreada = tareaService.saveTarea(new Tarea(tarea), usuario);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tareaCreada.getId())
                .toUri();

        return ResponseEntity.created(location).body(new DtoInfoTarea(tareaCreada));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DtoInfoTarea> editarTarea(@PathVariable Long id,
                                                 @RequestBody @Valid DtoEditarTarea datosEditarTarea,
                                                 @AuthenticationPrincipal Usuario usuario){
        try{
            var tarea = tareaService.updateTarea(new Tarea(datosEditarTarea), id, usuario);
            return ResponseEntity.ok(new DtoInfoTarea(tarea));
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/start")
    @Transactional
    public ResponseEntity<Map<String, String>> iniciarTarea(@PathVariable Long id){
        try {
            tareaService.iniciarTarea(id);
            return ResponseEntity.ok(Map.of("message", "Tarea iniciada exitosamente"));
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/ok")
    @Transactional
    public ResponseEntity<Map<String, String>> completarTarea(@PathVariable Long id){
        try {
            tareaService.completarTarea(id);
            return ResponseEntity.ok(Map.of("message", "Tarea completada exitosamente"));
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteTarea(@PathVariable Long id){
        try{
            tareaService.deleteTarea(id);
            return ResponseEntity.noContent().build();
        }catch(EntityNotFoundException e){
            return  ResponseEntity.notFound().build();
        }
    }
}
