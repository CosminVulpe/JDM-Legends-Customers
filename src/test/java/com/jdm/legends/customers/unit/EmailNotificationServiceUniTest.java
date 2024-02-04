package com.jdm.legends.customers.unit;

import com.jdm.legends.customers.service.ReminderEmailService;
import com.jdm.legends.customers.service.notification.EmailNotificationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.internet.MimeMessage;

import static com.jdm.legends.customers.utils.TestDummy.MAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailNotificationServiceUniTest extends JavaMailSenderImpl {
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private ReminderEmailService reminderEmailService;

    private EmailNotificationService emailNotificationService;

    @BeforeEach
    void init() {
        emailNotificationService = new EmailNotificationService(javaMailSender
                , "test@example.com"
                , reminderEmailService);
    }

    @Test
    void shouldSendEmailSuccessfully() {
        when(javaMailSender.createMimeMessage()).thenReturn(createMimeMessage());

        emailNotificationService.sendEmail(MAIL, 1L);

        verify(javaMailSender).send((MimeMessage) any());
        verify(reminderEmailService).saveReminderEmail(any());
    }

    @Test
    void shouldThrowExceptionWhenSendingTheEmail() {
        Long id = 1L;
        Assertions.assertThatThrownBy(() -> emailNotificationService.sendEmail(MAIL, id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error sending email to temporary customer " + id);
    }

    @Override
    public MimeMessage createMimeMessage() {
        return super.createMimeMessage();
    }
}
