package com.jdm.legends.customers.controller;

import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
public class CustomerSignController {
    private final CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<String> sign(Authentication authentication) {
        Optional<Customer> customerByEmailAddress = customerRepository
                .findCustomerByEmailAddress(authentication.getName());

        return customerByEmailAddress
                .map(customer -> ResponseEntity.ok(customer.getUserName()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
