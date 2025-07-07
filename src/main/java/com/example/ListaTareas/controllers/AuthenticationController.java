package com.example.ListaTareas.controllers;


import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.models.usuario.dto.DtoCredencialesUsuario;
import com.example.ListaTareas.repositories.UsuarioRepository;
import com.example.ListaTareas.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    TokenService tokenService,
                                    UsuarioRepository usuarioRepository,
                                    PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> authenticateUser(@RequestBody @Valid DtoCredencialesUsuario dtoCredencialesUsuario){
        Authentication authToken = new UsernamePasswordAuthenticationToken(dtoCredencialesUsuario.username(),
                dtoCredencialesUsuario.password());
        var authUser = authenticationManager.authenticate(authToken);

        var jwtToken = tokenService.generateToken((Usuario) authUser.getPrincipal());

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid
                                                                DtoCredencialesUsuario dtoCredencialesUsuario){
        if(usuarioRepository.findByUsername(dtoCredencialesUsuario.username()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).build();


        var newUser = new Usuario(dtoCredencialesUsuario.username(),
                passwordEncoder.encode(dtoCredencialesUsuario.password()));
        usuarioRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Usuario creado con exito"));
    }
}
