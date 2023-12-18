package com.jdm.legends.customers.service.notification;

import com.jdm.legends.customers.service.ReminderEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.internet.MimeMessage;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@Slf4j
public class EmailNotificationService {
    private final JavaMailSender javaMailSender;
    private final String emailFrom;
    private final ReminderEmailService reminderEmailService;

    public EmailNotificationService(JavaMailSender javaMailSender
            , @Value("${spring.mail.username}") String emailFrom
            , ReminderEmailService reminderEmailService) {
        this.javaMailSender = javaMailSender;
        this.emailFrom = emailFrom;
        this.reminderEmailService = reminderEmailService;
    }

    public void sendEmail(String toEmail, Long tempCustomerId) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom(emailFrom);
            messageHelper.setTo(toEmail);
            String emailBody = """
                    <p>Congrats , You were selected as the winner of the car! 🚗 😀</p>
                    <p>Please visit the following link to finish the order:</p>
                    <p><a href='http://localhost:8085/reminder-email?tempCustomerId=%d'>Click here to continue order</a></p>
                    <p>OR</p>
                    <p><a href='http://localhost:8080/car/cancelReservation/?tempCustomerId=%d'>Click here to cancel order</a></p>
                    <p>Best regards, JDM-Legends Team</p>
                    """.formatted(tempCustomerId, tempCustomerId);
            messageHelper.setText(emailBody, true);
            messageHelper.setSubject("Winner Car Bid JDM-Legends 🔰");

            javaMailSender.send(mimeMessage);

            log.info("Mail sent successfully to the winner {} ", "winner-user");
            reminderEmailService.saveReminderEmail(tempCustomerId);
        } catch (Exception e) {
            throw new EmailNotificationException("Error sending email to temporary customer " + tempCustomerId, e);
        }
    }

    @Slf4j
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    private static final class EmailNotificationException extends RuntimeException {
        public EmailNotificationException(String message, Throwable cause) {
            super(message, cause);
            log.error(message);
        }
    }
}
