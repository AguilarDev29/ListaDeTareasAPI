package com.example.ListaTareas.controllers;
import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.models.usuario.dto.DtoCambiarPassword;
import com.example.ListaTareas.models.usuario.dto.DtoCambiarEmail;
import com.example.ListaTareas.models.usuario.dto.DtoInfoUsuario;
import com.example.ListaTareas.models.usuario.dto.DtoRol;
import com.example.ListaTareas.services.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/v1/users")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<DtoInfoUsuario>> getUsuarios(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.getAll(pageable).map(DtoInfoUsuario::new));
    }

    @GetMapping("/enable")
    public ResponseEntity<Page<DtoInfoUsuario>> getUsuariosEnabled(Pageable pageable) {
        return ResponseEntity.ok(usuarioService.getAllEnabled(pageable).map(DtoInfoUsuario::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DtoInfoUsuario> getById(@PathVariable Long id) {
        try {
            var usuario = usuarioService.getUsuarioById(id);
            return ResponseEntity.ok(new DtoInfoUsuario(usuario));
        }catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<DtoInfoUsuario> createUsuario(@RequestBody Usuario usuario) {
        var nuevoUsuario = usuarioService.saveUsuario(usuario);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoUsuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(new DtoInfoUsuario(nuevoUsuario));
    }

    @PatchMapping("/rol/add/{id}")
    public ResponseEntity<Map<String, String>> addRol(@RequestBody DtoRol rol, @PathVariable Long id) {
        try{
            var usuarioToUpdate = usuarioService.getUsuarioById(id);
            if(!rol.nombre().isBlank()) usuarioService.addRol(rol.nombre(), id);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("message","Rol" + rol.nombre() + ", concedido exitosamente"));
    }

    @PatchMapping("/rol/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteRol(@RequestBody DtoRol rol, @PathVariable Long id) {
        try{
            var usuarioToUpdate = usuarioService.getUsuarioById(id);
            if(!rol.nombre().isBlank()) usuarioService.deleteRol(rol.nombre(), id);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("message","Rol" + rol.nombre() + ", revocado exitosamente"));
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

    @PatchMapping("/username/{id}")
    public ResponseEntity<Map<String, String>> changeEmailAdmin(@RequestBody DtoCambiarEmail email,
                                                                    @PathVariable Long id,
                                                                    @AuthenticationPrincipal Usuario usuario) {
        try{
            var usuarioToUpdate = usuarioService.getUsuarioById(id);
            if(!email.newEmail().isBlank())
                usuarioService.changeEmail(email.newEmail(), id, usuario);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("message","Nombre de usuario cambiado exitosamente"));
    }

    @PatchMapping("/password/{id}")
    public ResponseEntity<Map<String, String>> changePasswordAdmin(@RequestBody DtoCambiarPassword password,
                                                                    @PathVariable Long id,
                                                                    @AuthenticationPrincipal Usuario usuario) {
        try{
            var usuarioToUpdate = usuarioService.getUsuarioById(id);
            if(!password.newPassword().isBlank())
                usuarioService.changePassword(password.newPassword(), id, usuario);
        }catch(EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of("message", "Contraseña cambiada exitosamente"));
    }
}