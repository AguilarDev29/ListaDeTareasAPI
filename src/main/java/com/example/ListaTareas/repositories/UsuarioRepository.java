package com.example.ListaTareas.repositories;

import com.example.ListaTareas.models.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Page<Usuario> findAllByEnabled(Pageable pageable, Boolean enabled);

    Optional<Usuario> findByUsername(String username);
}
