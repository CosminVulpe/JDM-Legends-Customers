package com.jdm.legends.customers.unit;

import com.jdm.legends.customers.service.CustomerService;
import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import com.jdm.legends.customers.service.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.PersistenceException;
import java.util.Optional;

import static com.jdm.legends.customers.utils.TestDummy.getCustomMock;
import static com.jdm.legends.customers.utils.TestDummy.getCustomerRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceUnitTest {

    @Mock
    private CustomerRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void registerCustomerSuccessfully() {
        when(repository.findCustomerByEmailAddress(any())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(Customer.builder().id(1L).role(getCustomMock().getRole()).build());

        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(getCustomerRequest(), null);
        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    void registerCustomerReturnConflictCode() {
        when(repository.findCustomerByEmailAddress(any())).thenReturn(Optional.of(getCustomMock()));

        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(getCustomerRequest(), null);
        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(CONFLICT);
    }

    @Test
    void registerCustomerFailsWhenSaveInDB() {
        when(repository.findCustomerByEmailAddress(any())).thenReturn(Optional.empty());
        when(repository.save(any())).thenThrow(PersistenceException.class);

        ResponseEntity<HttpStatus> httpStatusResponseEntity = customerService.registerCustomer(getCustomerRequest(), null);
        assertThat(httpStatusResponseEntity.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }
}
