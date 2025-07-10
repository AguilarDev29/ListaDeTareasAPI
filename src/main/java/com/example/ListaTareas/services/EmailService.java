package com.example.ListaTareas.services;

import com.example.ListaTareas.models.others.DtoEmailBody;
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

    public boolean sendEmail(DtoEmailBody emailBody) throws MessagingException {
        return sendEmailTool(emailBody.content(),emailBody.email(), emailBody.subject());
    }

    private boolean sendEmailTool(String textMessage, String email,String subject) throws MessagingException {
        boolean send = false;
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        try {
            helper.setTo(email);
            helper.setText(textMessage, true);
            helper.setSubject(subject);
            sender.send(message);
            send = true;
            System.out.println("Mail enviado!");
        } catch (MessagingException e) {
            System.out.println("Error al enviar correo:");
            System.out.println(e.getMessage());
        }
        return send;
    }
}
