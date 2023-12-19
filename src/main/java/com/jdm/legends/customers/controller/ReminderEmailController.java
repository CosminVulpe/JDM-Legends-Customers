package com.jdm.legends.customers.controller;

import com.jdm.legends.customers.controller.dto.ReminderEmailDTO;
import com.jdm.legends.customers.service.ReminderEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/reminder-email")
public class ReminderEmailController {
    private final ReminderEmailService reminderEmailService;

    @GetMapping
    public void setReceiveTime(@RequestParam("tempCustomerId") Long tempCustomerId) {
        reminderEmailService.setReminderEmailReceiveEmail(tempCustomerId);
    }

    @GetMapping("/all")
    public List<ReminderEmailDTO> getAll() {
        return reminderEmailService.getAll();
    }

}
