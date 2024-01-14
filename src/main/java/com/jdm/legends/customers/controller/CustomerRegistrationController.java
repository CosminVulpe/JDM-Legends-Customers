package com.jdm.legends.customers.controller;

import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.CustomerRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register-customer")
public class CustomerRegistrationController {
    private final CustomerRegistrationService registrationService;

    @PostMapping
    public void registerCustomer(@RequestBody @Valid CustomerRequest request) {
        registrationService.registerCustomer(request);
    }
}
