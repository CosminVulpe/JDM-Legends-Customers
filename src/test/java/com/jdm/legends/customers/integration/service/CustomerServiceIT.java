package com.jdm.legends.customers.integration.service;

import antlr.collections.impl.IntRange;
import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.CustomerService;
import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import com.jdm.legends.customers.utils.TestDummy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static com.jdm.legends.customers.utils.TestDummy.getCustomMock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootTest
@ActiveProfiles("test-in-memory")
@Transactional
public class CustomerServiceIT {
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private CustomerService customerService;

    @Test
    void registerCustomerSuccessfully() {
        Customer customMock = getCustomMock();
        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(
                getCustomerRequest(customMock)
        );

        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    void registerCustomerFailsWhenCustomerIsPresentAlready() {
        Customer customer = repository.saveAndFlush(getCustomMock());
        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(getCustomerRequest(customer));
        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(CONFLICT);
    }

    private CustomerRequest getCustomerRequest(Customer customMock) {
        return new CustomerRequest(customMock.getFullName(), customMock.getUserName()
                , customMock.getRole().getRolesType(), customMock.getPhoneNumber(),
                customMock.getEmailAddress(), customMock.getPwd());
    }

}
