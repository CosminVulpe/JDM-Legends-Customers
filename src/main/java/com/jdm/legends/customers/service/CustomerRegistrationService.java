package com.jdm.legends.customers.service;

import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.mapping.CustomerMapper;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;

import static com.jdm.legends.customers.service.enums.Roles.CLIENT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerRegistrationService {
    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<HttpStatus> registerCustomer(CustomerRequest request) {
        Customer customer = CustomerMapper.INSTANCE.customerRequestToCustomerEntity(request);
        customer.setRole(CLIENT);
        customer.setPwd(passwordEncoder.encode(customer.getPwd()));

        try {
            Customer registeredCustomer = repository.save(customer);
            log.info("Customer id {} just registered with role {}", registeredCustomer.getId(), registeredCustomer.getRole());
            return ResponseEntity.status(CREATED).build();
        } catch (PersistenceException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

    }

}
