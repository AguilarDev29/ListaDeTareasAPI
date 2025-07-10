package com.example.ListaTareas.controllers;

import com.example.ListaTareas.models.codigoVerificacion.CodigoVerificacion;
import com.example.ListaTareas.models.others.DtoEmailBody;
import com.example.ListaTareas.models.usuario.Usuario;
import com.example.ListaTareas.services.CodigoVerificacionService;
import com.example.ListaTareas.services.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


@RestController
@RequestMapping("/v1/email")
public class EmailController {

    private final EmailService emailService;
    private final CodigoVerificacionService codigoVerificacionService;

    public EmailController(EmailService emailService, CodigoVerificacionService codigoVerificacionService) {
        this.emailService = emailService;
        this.codigoVerificacionService = codigoVerificacionService;
    }

    @PostMapping("/send")
    public ResponseEntity<Boolean> sendEmail(@RequestBody DtoEmailBody email){

        var emailBody = new DtoEmailBody(email.email(), "Codigo de verificacion",
                codigoVerificacionService.saveCodigo(new Usuario()));

        try{
            return ResponseEntity.ok(emailService.sendEmail(emailBody));
        }catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.notFound().build();
        }
    }
}
