package com.jdm.legends.customers.integration.service;

import com.jdm.legends.customers.service.ReminderEmailService;
import com.jdm.legends.customers.service.entity.ReminderEmail;
import com.jdm.legends.customers.service.notification.EmailNotificationService;
import com.jdm.legends.customers.service.repository.ReminderEmailRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jdm.legends.customers.utils.TestDummy.MAIL;

@SpringBootTest
@ActiveProfiles("test-in-memory")
@Transactional
public class EmailNotificationServiceIT {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ReminderEmailService reminderEmailService;
    @Autowired
    private ReminderEmailRepository reminderEmailRepository;

    private EmailNotificationService emailNotificationService;

    @BeforeEach
    void init() {
        emailNotificationService = new EmailNotificationService(javaMailSender
                , "test@example.com"
                , reminderEmailService);
    }

    @Test
    void shouldSendEmailSuccessfully() {
        emailNotificationService.sendEmail(MAIL, 1L);

        List<ReminderEmail> reminderEmailRepositoryAll = reminderEmailRepository.findAll();
        Assertions.assertThat(reminderEmailRepositoryAll).isNotEmpty();
    }

    @Test
    void shouldThrowExceptionWhenSendingTheEmail() {
        Long id = 1L;
        Assertions.assertThatThrownBy(() ->  emailNotificationService.sendEmail(null, id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error sending email to temporary customer " + id);
    }
}
