package com.example.ListaTareas.controllers;
import com.example.ListaTareas.models.others.DtoEmailBody;
import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.models.usuario.dto.DtoCredencialesUsuario;
import com.example.ListaTareas.repositories.UsuarioRepository;
import com.example.ListaTareas.services.CodigoVerificacionService;
import com.example.ListaTareas.services.EmailService;
import com.example.ListaTareas.services.TokenService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final CodigoVerificacionService codigoVerificacionService;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    TokenService tokenService,
                                    UsuarioRepository usuarioRepository,
                                    PasswordEncoder passwordEncoder,
                                    EmailService emailService,
                                    CodigoVerificacionService codigoVerificacionService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.codigoVerificacionService = codigoVerificacionService;
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> authenticateUser(@RequestBody @Valid
                                                                   DtoCredencialesUsuario dtoCredencialesUsuario){
        Authentication authToken = new UsernamePasswordAuthenticationToken(dtoCredencialesUsuario.email(),
                dtoCredencialesUsuario.password());
        var authUser = authenticationManager.authenticate(authToken);

        var jwtToken = tokenService.generateToken((Usuario) authUser.getPrincipal());

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid
                                                                DtoCredencialesUsuario dtoCredencialesUsuario) throws MessagingException {
        var newUser = new Usuario(dtoCredencialesUsuario.email(),
                passwordEncoder.encode(dtoCredencialesUsuario.password()));

        if(usuarioRepository.findByEmail(dtoCredencialesUsuario.email()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        newUser = usuarioRepository.save(newUser);

        var codigoVerificacion = codigoVerificacionService.saveCodigo(newUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/confirm/{id}/{codigo}")
                .buildAndExpand(newUser.getId(), codigoVerificacion)
                .toUri();

        var emailBody = new DtoEmailBody(dtoCredencialesUsuario.email(),
                "Código de verificacion", "<p>Su código de verificación es: <strong>" +
                codigoVerificacion + "</strong></p>" +
                "<p>Este código es válido por 15 minutos.</p>" +
                "<p>Por favor, ingrese al siguiente link para confirmar su cuenta:</p>" +
                "<a href=\"" + location.toString() + "\">Confirmar mi cuenta</a>");

        if(emailService.sendEmail(emailBody))
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Usuario creado con exito," +
                            " por favor revise su casilla de correo electronico" +
                            " y siga las instrucciones para confirmar su cuenta"));

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/register/confirm/{id}/{codigo}")
    public ResponseEntity<Map<String, String>> confirmUser(@PathVariable Long id, @PathVariable String codigo){
        if(codigoVerificacionService.existByCodigo(codigo)){
            var user = usuarioRepository.findById(id);
            if(user.isPresent()){
                user.get().setEnabled(true);
                usuarioRepository.save(user.get());
                codigoVerificacionService.deshabilitarCodigo(codigo);
                return ResponseEntity.ok(Map.of("message", "Usuario confirmado con exito"));
            }
        }
        return ResponseEntity.notFound().build();

    }
}
