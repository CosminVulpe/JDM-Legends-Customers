package com.jdm.legends.customers.integration.service;

import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.CustomerService;
import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import com.jdm.legends.customers.service.repository.TemporaryCustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.jdm.legends.customers.utils.TestDummy.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest
@ActiveProfiles("test-in-memory")
@Transactional
public class CustomerServiceIT {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private TemporaryCustomerRepository temporaryCustomerRepository;

    @Autowired
    private CustomerService customerService;

    @Test
    void registerNewCustomer() {
        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(getCustomerRequest(), null);
        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    void registerCustomerFailsWhenCustomerIsPresentAlready() {
        repository.saveAndFlush(getCustomMock());
        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(getCustomerRequest(), null);
        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(CONFLICT);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "null"})
    void registerCustomerFailsWhenFieldsAreEmpty(String value) {
        boolean isNull = value.equals("null");
        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(isNull ? mock(null, null) : mock(value, value)
                , null);

        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(NOT_ACCEPTABLE);
    }

    @Test
    void registerNewFullCustomer() {
        TemporaryCustomer tempCustomerMock = getTempCustomerMock();
        tempCustomerMock.setHistoryBidId("1");
        tempCustomerMock.setOrderId(1L);
        TemporaryCustomer temporaryCustomer = temporaryCustomerRepository.save(tempCustomerMock);

        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(getCustomerRequest(), temporaryCustomer.getId());
        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(CREATED);

        Optional<TemporaryCustomer> optionalTemporaryCustomer = temporaryCustomerRepository.findById(temporaryCustomer.getId());
        assertThat(optionalTemporaryCustomer).isEmpty();

        Optional<Customer> customerByEmailAddress = repository.findCustomerByEmailAddress(temporaryCustomer.getEmailAddress());
        assertThat(customerByEmailAddress).isPresent();
    }

    @Test
    void registerNewFullCustomerThrowsExceptionWhenTempCustomerEmpty() {
        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(getCustomerRequest(), 100L);
        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    void registerNewFullCustomerThrowsExceptionWhenTempCustomerHasOrderIdAndHistoryBidEmpty() {
        TemporaryCustomer temporaryCustomer = temporaryCustomerRepository.save(getTempCustomerMock());

        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(getCustomerRequest(), temporaryCustomer.getId());
        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(BAD_REQUEST);
    }


    private CustomerRequest mock(String fullName, String userName) {
        Customer customMock = getCustomMock();
        return new CustomerRequest(fullName, userName
                , customMock.getRole().getRolesType(), customMock.getPhoneNumber(),
                customMock.getEmailAddress(), customMock.getPwd());
    }
}
