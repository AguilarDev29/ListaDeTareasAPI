package com.example.ListaTareas.services;

import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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

    public void changeUsername(String username, Long id){
        var usuarioToUpdate = usuarioRepository.findById(id);

        if(usuarioToUpdate.isPresent()){
            if(username != null)
                usuarioToUpdate.get().setUsername(username);
            usuarioRepository.save(usuarioToUpdate.get());
        }
    }

    public void changePassword(String password, Long id){
        var usuarioToUpdate = usuarioRepository.findById(id);

        if(usuarioToUpdate.isPresent()){
            if(password != null)
                usuarioToUpdate.get().setPassword(password);
            usuarioRepository.save(usuarioToUpdate.get());
        }
    }

    public void addRol(Usuario.Rol rol, Long id){
        var usuarioToUpdate = usuarioRepository.findById(id);

        if(usuarioToUpdate.isPresent()){
            if(rol != null && usuarioToUpdate.get().getRoles().contains(rol))
                usuarioToUpdate.get().setRoles(rol);
            usuarioRepository.save(usuarioToUpdate.get());
        }
    }

    public void deleteRol(Usuario.Rol rol, Long id){
        var usuarioToUpdate = usuarioRepository.findById(id);

        if(usuarioToUpdate.isPresent()){
            if(rol != null) usuarioToUpdate.get().getRoles().remove(rol);
            usuarioRepository.save(usuarioToUpdate.get());
        }
    }

    public void deleteUsuario(Long id){
        usuarioRepository.deleteById(id);
    }
}
