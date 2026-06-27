package com.storeagent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(@Autowired(required = false) JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRegistrationEmail(String toEmail, String username, String role) {
        String subject = "Welcome to the Branded Shoe Store AI Agent System!";
        String body = String.format(
                "Hello %s,\n\n" +
                "Welcome to the Branded Shoe Store AI Agent! You have successfully registered as a %s.\n\n" +
                "You can now query our agent about inventory tracking (Campus, Nike, Skechers, Puma, Sparx), " +
                "request price comparisons, and look up order statuses in real-time.\n\n" +
                "Best regards,\n" +
                "The Branded Shoe Store Team",
                username, role
        );
        sendCustomEmail(toEmail, subject, body);
    }

    public void sendCustomEmail(String toEmail, String subject, String body) {
        System.out.println("========== SIMULATED EMAIL OUTBOUND ==========");
        System.out.println("To: " + toEmail);
        System.out.println("Subject: " + subject);
        System.out.println("Body:\n" + body);
        System.out.println("==============================================");

        if (mailSender != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject(subject);
                message.setText(body);
                mailSender.send(message);
                System.out.println("Email sent successfully using SMTP.");
            } catch (Exception e) {
                System.err.println("SMTP Email failed (this is expected if SMTP environment variables are not set): " + e.getMessage());
            }
        } else {
            System.out.println("JavaMailSender is not initialized. Skipping SMTP send (Simulated log outputted).");
        }
    }
}
