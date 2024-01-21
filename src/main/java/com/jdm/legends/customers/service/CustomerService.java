package com.jdm.legends.customers.service;

import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.entity.Role;
import com.jdm.legends.customers.service.mapping.CustomerMapper;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import com.jdm.legends.customers.service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;

import java.util.List;
import java.util.Optional;

import static com.jdm.legends.customers.service.enums.RolesType.CLIENT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public ResponseEntity<HttpStatus> registerCustomer(CustomerRequest request) {
        Optional<Customer> customerByEmailAddress = repository.findCustomerByEmailAddress(request.emailAddress());
        if (customerByEmailAddress.isPresent()) {
            log.warn("Username {} already in the database", customerByEmailAddress.get().getEmailAddress());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Customer customer = CustomerMapper.INSTANCE.customerRequestToCustomerEntity(request);
        Role role = new Role(request.rolesTypes() == null || request.rolesTypes().isEmpty()
                ? List.of(CLIENT)
                : request.rolesTypes());
        roleRepository.save(role);

        customer.setRole(role);
        customer.setPwd(passwordEncoder.encode(customer.getPwd()));
        try {
            Customer registeredCustomer = repository.save(customer);
            log.info("Customer id {} just registered with role(s) {}", registeredCustomer.getId(), registeredCustomer.getRole().getRolesType());
            return ResponseEntity.status(CREATED).build();
        } catch (PersistenceException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }

    }

}
