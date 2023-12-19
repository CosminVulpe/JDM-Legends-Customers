package com.jdm.legends.customers.controller.dto;

import java.time.LocalDateTime;

public record ReminderEmailDTO(
        LocalDateTime sentTimeEmail,
        LocalDateTime enterTimeEmail,
        LocalDateTime deadLineEmail,
        Long temporaryCustomerId
) {
}
