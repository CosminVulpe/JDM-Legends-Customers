package com.jdm.legends.customers.controller;

import com.jdm.legends.customers.controller.dto.CustomerSignRequest;
import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CustomerSignController {
    private final CustomerRepository customerRepository;

    @PostMapping("/sign")
    public ResponseEntity<HttpStatus> sign(@RequestBody @Valid CustomerSignRequest request) {
        Optional<Customer> customerByEmailAddress = customerRepository
                .findCustomerByEmailAddress(request.emailAddress());

        return (customerByEmailAddress.isPresent()) ?
                ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}
