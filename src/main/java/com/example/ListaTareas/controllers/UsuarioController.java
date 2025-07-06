package com.example.ListaTareas.controllers;

import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.models.usuario.dto.DtoCambiarPassword;
import com.example.ListaTareas.models.usuario.dto.DtoCambiarUsername;
import com.example.ListaTareas.services.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<Page<Usuario>> getUsuarios(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.getAll(pageable));
    }

    @GetMapping("/enable")
    public ResponseEntity<Page<Usuario>> getUsuariosEnabled(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.getAllEnabled(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        try {
            var usuario = usuarioService.getUsuarioById(id);
            return ResponseEntity.ok(usuario);
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        var nuevoUsuario = usuarioService.saveUsuario(usuario);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoUsuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(nuevoUsuario);
    }

    @PatchMapping("/username/{id}")
    public ResponseEntity<String> changeUsername(@RequestBody DtoCambiarUsername username, @PathVariable Long id) {
        try{
            var usuarioToUpdate = usuarioService.getUsuarioById(id);
            if(!username.newUsername().isBlank())
                usuarioService.changePassword(username.newUsername(), id);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Nombre de usuario cambiado exitosamente");
    }

    @PatchMapping("/password/{id}")
    public ResponseEntity<String> changePassword(@RequestBody DtoCambiarPassword password, @PathVariable Long id) {
        try{
            var usuarioToUpdate = usuarioService.getUsuarioById(id);
            if(!password.newPassword().isBlank())
                usuarioService.changePassword(password.newPassword(), id);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Contraseña cambiada exitosamente");
    }

    @PatchMapping("/rol/add/{id}")
    public ResponseEntity<String> addRol(@RequestBody Usuario.Rol rol, @PathVariable Long id) {
        try{
            var usuarioToUpdate = usuarioService.getUsuarioById(id);
            if(!rol.name().isBlank()) usuarioService.addRol(rol, id);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Rol" + rol + ", concedido exitosamente");

    }

    @PatchMapping("/rol/delete/{id}")
    public ResponseEntity<String> deleteRol(@RequestBody Usuario.Rol rol, @PathVariable Long id) {
        try{
            var usuarioToUpdate = usuarioService.getUsuarioById(id);
            if(!rol.name().isBlank()) usuarioService.deleteRol(rol, id);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Rol" + rol + ", eliminado exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        try {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.noContent().build();
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

}