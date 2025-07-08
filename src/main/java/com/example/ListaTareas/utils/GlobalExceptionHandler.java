package com.example.ListaTareas.utils;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Error para cuando el token es inválido, ha expirado o tiene la firma mal
    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<Map<String, Object>> handleJwtVerificationException(JWTVerificationException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                "Token inválido o expirado.");
    }

    // Error para cuando el usuario intenta autenticarse con credenciales incorrectas
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED,
                "Credenciales incorrectas.");
    }

    // Error para cuando un usuario autenticado intenta acceder a un recurso al que no tiene permiso
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN,
                "Acceso denegado. No tienes los permisos necesarios.");
    }

    // Un error genérico para cualquier otra cosa
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado en el servidor.");
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );
        return new ResponseEntity<>(body, status);
    }

}
