package com.jdm.legends.customers.service;

import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.entity.Role;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import com.jdm.legends.customers.service.mapping.CustomerMapper;
import com.jdm.legends.customers.service.mapping.TemporaryCustomerMapper;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import com.jdm.legends.customers.service.repository.TemporaryCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static com.jdm.legends.customers.service.enums.RolesType.CLIENT;
import static org.springframework.http.HttpStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TemporaryCustomerRepository temporaryCustomerRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public ResponseEntity<HttpStatus> registerCustomer(CustomerRequest request, Long tempCustomerId) {
        if (tempCustomerId == null && request != null) {
            Optional<Customer> customerByEmailAddress = repository.findCustomerByEmailAddress(request.emailAddress());
            if (customerByEmailAddress.isPresent()) {
                log.warn("Username {} already in the database", customerByEmailAddress.get().getEmailAddress());
                return ResponseEntity.status(CONFLICT).build();
            }

            if (checkRegistrationFields(request)) {
                log.warn("Request for registration {} has empty fields", request);
                return ResponseEntity.status(NOT_ACCEPTABLE).build();
            }

            return createNewAccount(request);
        }
        return registerTempCustomerToNewFullCustomer(request, tempCustomerId);
    }

    private ResponseEntity<HttpStatus> registerTempCustomerToNewFullCustomer(CustomerRequest request, Long tempCustomerId) {
        Optional<TemporaryCustomer> optionalTemporaryCustomer = temporaryCustomerRepository.findById(tempCustomerId);
        if (optionalTemporaryCustomer.isEmpty()) {
            log.warn("Temp Customer not found with specific id");
            return ResponseEntity.status(NOT_FOUND).build();
        }

        TemporaryCustomer temporaryCustomer = optionalTemporaryCustomer.get();
        if (temporaryCustomer.getHistoryBidId() == null && temporaryCustomer.getOrderId() == null) {
            return ResponseEntity.status(BAD_REQUEST).build();
        }

        Customer customer = TemporaryCustomerMapper.INSTANCE.tempCustomerToCustomer(temporaryCustomer);
        customer.setPhoneNumber(request.phoneNumber());
        customer.setPwd(passwordEncoder.encode(request.pwd()));
        customer.setRole(new Role(List.of(CLIENT)));

        Customer mappedCustomer = customerRepository.save(customer);
        log.info("Successfully converting temp customer id {} to fully customer {}"
                , tempCustomerId, mappedCustomer.getId());

        temporaryCustomerRepository.delete(temporaryCustomer);
        log.info("Deleting temporary customer");

        return ResponseEntity.status(CREATED).build();
    }

    private ResponseEntity<HttpStatus> createNewAccount(CustomerRequest request) {
        Customer customer = CustomerMapper.INSTANCE.customerRequestToCustomerEntity(request);
        Role role = new Role(request.rolesTypes() == null || request.rolesTypes().isEmpty()
                ? List.of(CLIENT)
                : request.rolesTypes());

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

    private boolean checkRegistrationFields(CustomerRequest request) {
        return (request.fullName() == null || request.userName() == null)
                || (request.fullName() != null && request.fullName().isBlank())
                || (request.userName() != null && request.userName().isBlank());

    }

}
