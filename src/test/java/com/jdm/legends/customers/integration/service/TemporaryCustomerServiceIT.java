package com.jdm.legends.customers.integration.service;

import com.jdm.legends.customers.controller.dto.TemporaryCustomerDTO;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerIdResponse;
import com.jdm.legends.customers.repository.TemporaryCustomerRepository;
import com.jdm.legends.customers.service.TemporaryCustomerService;
import com.jdm.legends.customers.service.TemporaryCustomerService.TemporaryCustomerByIdException;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jdm.legends.customers.utils.TestDummy.getTemporaryCustomerRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test-in-memory")
@Sql("/add-temporary-customers.sql")
@Transactional
class TemporaryCustomerServiceIT {

    @Autowired
    private TemporaryCustomerRepository repository;

    @Autowired
    private TemporaryCustomerService service;

    @Test
    void getAllTempCustomersSuccessfully() {
        List<TemporaryCustomer> allTempCustomers = service.getAllTempCustomers();
        assertThat(allTempCustomers).isNotEmpty();
        allTempCustomers.forEach(temporaryCustomer -> assertThat(temporaryCustomer).isNotNull());
    }

    @Test
    void getTempCustomerByIdSuccessfully() {
        TemporaryCustomer temporaryCustomer = repository.findAll().get(0);
        TemporaryCustomerDTO tempCustomerById = service.getTempCustomerById(temporaryCustomer.getId());
        assertThat(tempCustomerById).isNotNull();
    }

    @Test
    void getTempCustomerByIdThrowsExceptionWhenIdIsNotFound() {
        assertThatThrownBy(() -> service.getTempCustomerById(100L))
                .isInstanceOf(TemporaryCustomerByIdException.class)
                .hasMessage("Temporary Customer with specific id cannot be found");
    }

    @Test
    void saveTempCustomerSuccessfully() {
        TemporaryCustomerIdResponse temporaryCustomerIdResponse = service.saveTempCustomer(getTemporaryCustomerRequest(), 1L);
        assertThat(temporaryCustomerIdResponse).isNotNull();
        assertThat(temporaryCustomerIdResponse.id()).isNotNull();
    }
}
