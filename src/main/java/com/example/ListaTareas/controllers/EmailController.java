package com.example.ListaTareas.controllers;

import com.example.ListaTareas.models.DtoEmailBody;
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

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<Boolean> sendEmail(@RequestBody DtoEmailBody emailBody){

        try{
            return ResponseEntity.ok(emailService.sendEmail(emailBody));
        }catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.notFound().build();
        }
    }
}
