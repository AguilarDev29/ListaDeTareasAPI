package com.example.ListaTareas.services;
import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<Usuario> getAll(Pageable pageable){
        return usuarioRepository.findAll(pageable);
    }
    
    public Page<Usuario> getAllEnabled(Pageable pageable){
        return usuarioRepository.findAllByEnabled(pageable, true);
    }

    public Usuario getUsuarioById(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    public Usuario saveUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public void changeUsername(String username, Long id, Usuario usuario){

        if(usuario.getRoles().size() == 1 && usuario.getRoles().contains("USER")){
            var usuarioToUpdate = usuarioRepository.findById(usuario.getId());
            usuarioToUpdate.ifPresent(value -> value.setUsername(username));
        }

        var usuarioToUpdate = usuarioRepository.findById(id);

        if(usuarioToUpdate.isPresent()){
            if(username != null)
                usuarioToUpdate.get().setUsername(username);
            usuarioRepository.save(usuarioToUpdate.get());
        }
    }

    public void changePassword(String password, Long id, Usuario usuario){

        if(usuario.getRoles().size() == 1 && usuario.getRoles().contains("USER")){
            var usuarioToUpdate = usuarioRepository.findById(usuario.getId());
            usuarioToUpdate.ifPresent(value -> {
                value.setPassword(passwordEncoder.encode(password));
            });
        }

        var usuarioToUpdate = usuarioRepository.findById(id);

        if(usuarioToUpdate.isPresent()){
            if(password != null)
                usuarioToUpdate.get().setPassword(passwordEncoder.encode(password));
            usuarioRepository.save(usuarioToUpdate.get());
        }
    }

    public void addRol(String rol, Long id) {
        if (rol.isBlank()) throw new IllegalArgumentException("El rol no puede ser nulo");

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        if (!usuario.getRoles().contains(rol)) {
            usuario.getRoles().add(rol);
            usuarioRepository.save(usuario);
        }
    }

    public void deleteRol(String rol, Long id) {
        if (rol.isBlank()) throw new IllegalArgumentException("El rol no puede ser nulo");

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        if (usuario.getRoles().remove(rol)) {

            if (usuario.getRoles().isEmpty()) usuario.getRoles().add(Usuario.Rol.USER.name());
            usuarioRepository.save(usuario);
        }
    }

    public void deleteUsuario(Long id){
        usuarioRepository.deleteById(id);
    }
}