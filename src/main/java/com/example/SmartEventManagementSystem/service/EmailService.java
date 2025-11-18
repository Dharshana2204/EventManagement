package com.example.SmartEventManagementSystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username:no-reply@example.com}")
    private String fromEmail;

    public void sendLoginEmail(String toEmail, String name) {

        if (toEmail == null || toEmail.trim().isEmpty()) {
            logger.error("EmailService: toEmail is NULL/EMPTY. Skipping sending email.");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(toEmail.trim());
            message.setSubject("Login Successful");

            String safeName = (name == null ? "" : name);

            message.setText(
                    "Hi " + safeName + ",\n\n" +
                            "You have successfully logged in.\n\n" +
                            "Regards,\nFestEvents Team"
            );

            mailSender.send(message);
            logger.info("Login email sent successfully to {}", toEmail);

        } catch (Exception e) {
            logger.error(" Failed to send login email: {}", e.getMessage());
        }
    }
}
