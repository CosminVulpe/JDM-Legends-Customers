package com.jdm.legends.customers.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationService {
    private final JavaMailSender javaMailSender;
    private final String emailFrom;

    public EmailNotificationService(JavaMailSender javaMailSender, @Value("${spring.mail.username}") String emailFrom) {
        this.javaMailSender = javaMailSender;
        this.emailFrom = emailFrom;
    }

    public void sendEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(toEmail);
        message.setText("""
                Congrats , You were selected as the winner of the car! 🚗 😀\s
                Please visit the following link to finish the order: www.google.com
                """);
        message.setSubject("Winner Car Bid JDM-Legends 🔰");
        javaMailSender.send(message);
        log.info("Mail sent successfully to the winner {} ", "winner-user");
    }
}
