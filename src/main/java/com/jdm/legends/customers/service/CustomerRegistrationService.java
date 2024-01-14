package com.jdm.legends.customers.service;

import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.mapping.CustomerMapper;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerRegistrationService {
    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void registerCustomer(CustomerRequest request) {
        Customer customer = CustomerMapper.INSTANCE.customerRequestToCustomerEntity(request);
        customer.setPwd(passwordEncoder.encode(customer.getPwd()));

        Customer registeredCustomer = repository.save(customer);
        log.info("Customer id {} just registered with role {}", registeredCustomer.getId(), registeredCustomer.getRole());
    }

}
