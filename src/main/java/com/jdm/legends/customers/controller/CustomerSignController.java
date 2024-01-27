package com.jdm.legends.customers.controller;

import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
public class CustomerSignController {
    private final CustomerRepository customerRepository;

    @GetMapping
    public String sign(Authentication authentication) {
        return customerRepository
                .findCustomerByEmailAddress(authentication.getName())
                .map(Customer::getUserName).orElse("");
    }
}
