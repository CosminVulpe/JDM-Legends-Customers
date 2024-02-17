package com.jdm.legends.customers.controller;

import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register-customer")
public class CustomerRegistrationController {
    private final CustomerService registrationService;

    @PostMapping
    public ResponseEntity<HttpStatus> registerCustomer(@RequestBody @Valid CustomerRequest request
            , @RequestParam(value = "tempCustomerId", required = false) Long tempCustomerId) {
       return registrationService.registerCustomer(request, tempCustomerId);
    }
}
