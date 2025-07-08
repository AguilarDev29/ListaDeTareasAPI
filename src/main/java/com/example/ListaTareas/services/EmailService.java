package com.example.ListaTareas.services;

import com.example.ListaTareas.models.DtoEmailBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService{


    private final JavaMailSender sender;

    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    public boolean sendEmail(DtoEmailBody emailBody)  {
        return sendEmailTool(emailBody.content(),emailBody.email(), emailBody.subject());
    }

    private boolean sendEmailTool(String textMessage, String email,String subject) {
        boolean send = false;
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(email);
            helper.setText(textMessage, true);
            helper.setSubject(subject);
            sender.send(message);
            send = true;
            System.out.println("Mail enviado!");
        } catch (Exception e) {
            System.out.println("Error al enviar correo:");
            e.printStackTrace(); // Para ver el mensaje exacto del error
        }
        return send;
    }
}
