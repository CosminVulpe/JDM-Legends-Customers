package com.jdm.legends.customers.integration.service;

import com.jdm.legends.customers.repository.ReminderEmailRepository;
import com.jdm.legends.customers.repository.TemporaryCustomerRepository;
import com.jdm.legends.customers.service.ReminderEmailService;
import com.jdm.legends.customers.service.entity.ReminderEmail;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import com.jdm.legends.customers.utils.TestDummy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test-in-memory")
@Sql("/add-temporary-customers.sql")
@Transactional
public class ReminderEmailServiceIT {

    @Autowired
    private ReminderEmailRepository reminderEmailRepository;

    @Autowired
    private ReminderEmailService reminderEmailService;

    @Autowired
    private TemporaryCustomerRepository temporaryCustomerRepository;

    private static TemporaryCustomer temporaryCustomer;

    @BeforeEach
    void setUp() {
        reminderEmailRepository.save(TestDummy.getReminderEmailMock());
        temporaryCustomer = temporaryCustomerRepository.findAll().get(0);
    }

    @Test
    void shouldSetEmailEnteringLink() {
        ReminderEmail reminderEmail = reminderEmailRepository.findAll().get(0);
        reminderEmail.setTemporaryCustomerId(temporaryCustomer.getId());

        reminderEmailService.setReminderEmailReceiveEmail(temporaryCustomer.getId());
        assertThat(reminderEmail.getEnterTimeEmail()).isNotNull();
    }

    @Test
    void shouldSetEnterEmailTimeNullWhenDeadlineIsPassed() {
        ReminderEmail reminderEmail = reminderEmailRepository.findAll().get(0);
        reminderEmail.setTemporaryCustomerId(temporaryCustomer.getId());
        reminderEmail.setDeadLineEmail(now().minusYears(6));

        reminderEmailService.setReminderEmailReceiveEmail(temporaryCustomer.getId());
        assertThat(reminderEmail.getEnterTimeEmail()).isNull();
    }

    @Test
    void shouldThrowExceptionWhenSetReminderEmailReceiveEmail() {
        long temporaryCustomerId = 110L;
        ReminderEmail reminderEmail = reminderEmailRepository.findAll().get(0);
        reminderEmail.setTemporaryCustomerId(22L);
        assertThatThrownBy(() -> reminderEmailService.setReminderEmailReceiveEmail(temporaryCustomerId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Reminder Email cannot be found with specific temporary customer id "+ temporaryCustomerId);
    }

    @Test
    void shouldSaveReminderEmail() {
        reminderEmailService.saveReminderEmail(temporaryCustomer.getId());

        assertThat(reminderEmailRepository.findAll()).hasSize(2);
    }
}
