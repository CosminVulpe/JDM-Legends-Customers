package com.jdm.legends.customers.service;

import com.jdm.legends.customers.controller.dto.ReminderEmailDTO;
import com.jdm.legends.customers.repository.ReminderEmailRepository;
import com.jdm.legends.customers.service.entity.ReminderEmail;
import com.jdm.legends.customers.service.mapping.ReminderEmailMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderEmailService {

    private final ReminderEmailRepository reminderEmailRepository;

    public void setReminderEmailReceiveEmail(Long tempCustomerId) {
        ReminderEmail reminderEmail = reminderEmailRepository.findAll().stream()
                .filter(item -> item.getTemporaryCustomerId().equals(tempCustomerId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Reminder Email cannot be found with specific temporary customer id " + tempCustomerId));

        LocalDateTime enterTimeEmail = now();
        if (!reminderEmail.isDeadLinePassed(enterTimeEmail)) {
            reminderEmail.setEnterTimeEmail(enterTimeEmail);
            reminderEmailRepository.save(reminderEmail);
            return;
        }
        log.warn("Deadline for temporary customer {} passed", tempCustomerId);
    }

    public List<ReminderEmailDTO> getAll() {
        return reminderEmailRepository.findAll().stream()
                .map(ReminderEmailMapper.INSTANCE::remainderEmailEntityToRemainderEmailDTO).toList();
    }

    public void saveReminderEmail(Long temporaryCustomerId) {
        LocalDateTime now = now();
        ReminderEmail reminderEmail = ReminderEmail.builder()
                .sentTimeEmail(now)
                .deadLineEmail(now.plusHours(24))
                .temporaryCustomerId(temporaryCustomerId)
                .build();
        reminderEmailRepository.save(reminderEmail);
        log.info("Reminder Email save for temporary customer id {} deadline at {}", temporaryCustomerId
                , reminderEmail.getDeadLineEmail().format(
                        DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                ));
    }
}
