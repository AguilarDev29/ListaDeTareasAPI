package com.example.ListaTareas.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ListaTareas.models.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    public Map<String, String> generateToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            var token = JWT.create()
                    .withIssuer("Lista de tareas API")
                    .withSubject(usuario.getUsername())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);

            return Map.of("JWT", token);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar el token");
        }
    }

    public String getSubject(String token){
        if(token == null){
            throw new RuntimeException("Token nulo");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            DecodedJWT verifier = JWT.require(algorithm)
                    .withIssuer("Lista de tareas API")
                    .build()
                    .verify(token);

            return verifier.getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT invalido o expirado", exception);
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.ofHours(-3));
    }
}
