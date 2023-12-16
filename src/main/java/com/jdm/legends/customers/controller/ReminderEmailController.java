package com.jdm.legends.customers.controller;

import com.jdm.legends.customers.service.ReminderEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/reminder-email")
public class ReminderEmailController {
    private final ReminderEmailService reminderEmailService;

    @GetMapping
    public void setReceiveTime(@RequestParam("tempCustomerId") Long tempCustomerId) {
        reminderEmailService.setReminderEmailReceiveEmail(tempCustomerId);
    }

}
