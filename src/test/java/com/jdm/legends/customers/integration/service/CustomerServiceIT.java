package com.jdm.legends.customers.integration.service;

import com.jdm.legends.customers.service.CustomerService;
import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.jdm.legends.customers.utils.TestDummy.getCustomMock;
import static com.jdm.legends.customers.utils.TestDummy.getCustomerRequest;
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
        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(getCustomerRequest());
        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    void registerCustomerFailsWhenCustomerIsPresentAlready() {
        repository.saveAndFlush(getCustomMock());
        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(getCustomerRequest());
        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(CONFLICT);
    }
}
