package com.example.SmartEventManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendLoginEmail(String toEmail, String name) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("yourgmail@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Login Successful");
        message.setText(
                "Hi " + name + ",\n\n" +
                        "You have successfully logged in.\n\n" +
                        "Regards,\nFestEvents Team"
        );

        mailSender.send(message);
    }
}
