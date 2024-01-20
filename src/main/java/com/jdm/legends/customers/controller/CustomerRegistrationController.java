package com.jdm.legends.customers.controller;

import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.CustomerRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register-customer")
public class CustomerRegistrationController {
    private final CustomerRegistrationService registrationService;

    @PostMapping
    public ResponseEntity<HttpStatus> registerCustomer(@RequestBody @Valid CustomerRequest request) {
       return registrationService.registerCustomer(request);
    }
}
