package com.jdm.legends.customers.unit;

import com.jdm.legends.customers.service.repository.ReminderEmailRepository;
import com.jdm.legends.customers.service.ReminderEmailService;
import com.jdm.legends.customers.service.entity.ReminderEmail;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static com.jdm.legends.customers.utils.TestDummy.getReminderEmailMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReminderEmailServiceUnitTest {
    @Mock
    private ReminderEmailRepository reminderEmailRepository;

    @InjectMocks
    private ReminderEmailService reminderEmailService;

    @Test
    void shouldSetReceiveReminderEmail() {
        ReminderEmail reminderEmailMock = getReminderEmailMock();
        reminderEmailMock.setTemporaryCustomerId(1L);

        when(reminderEmailRepository.findAll()).thenReturn(List.of(reminderEmailMock));

        reminderEmailService.setReminderEmailReceiveEmail(1L);

        verify(reminderEmailRepository).save(any());
    }


    @Test
    void shouldNotSetReceiveReminderEmailWhenDeadlineIsNotPassed() {
        ReminderEmail reminderEmailMock = getReminderEmailMock();
        reminderEmailMock.setTemporaryCustomerId(1L);
        reminderEmailMock.setDeadLineEmail(LocalDateTime.now().minusYears(10));

        when(reminderEmailRepository.findAll()).thenReturn(List.of(reminderEmailMock));

        reminderEmailService.setReminderEmailReceiveEmail(1L);

        verify(reminderEmailRepository, never()).save(any());
    }


    @Test
    void shouldThrowExceptionWhenReminderEmailNotFound() {
        ReminderEmail reminderEmailMock = getReminderEmailMock();
        reminderEmailMock.setTemporaryCustomerId(5L);
        when(reminderEmailRepository.findAll()).thenReturn(List.of(reminderEmailMock));

        long tempCustomerId = 100L;
        Assertions.assertThatThrownBy(() ->  reminderEmailService.setReminderEmailReceiveEmail(tempCustomerId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Reminder Email cannot be found with specific temporary customer id " + tempCustomerId);
    }
}
