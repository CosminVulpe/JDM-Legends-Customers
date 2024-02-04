package com.jdm.legends.customers.unit;

import com.jdm.legends.customers.controller.dto.OrderIdRequest;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerDTO;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerIdResponse;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.customers.service.repository.TemporaryCustomerRepository;
import com.jdm.legends.customers.service.TemporaryCustomerService;
import com.jdm.legends.customers.service.TemporaryCustomerService.TemporaryCustomerByIdException;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.jdm.legends.customers.utils.TestDummy.FULL_NAME;
import static com.jdm.legends.customers.utils.TestDummy.MAIL;
import static com.jdm.legends.customers.utils.TestDummy.USERNAME;
import static com.jdm.legends.customers.utils.TestDummy.getTempCustomerMock;
import static com.jdm.legends.customers.utils.TestDummy.getTemporaryCustomerRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TemporaryCustomerServiceUnitTest {
    @Mock
    private TemporaryCustomerRepository repository;

    @InjectMocks
    private TemporaryCustomerService temporaryCustomerService;


    @Test
    void getAllTempUsersSuccessfully() {
        when(repository.findAll()).thenReturn(List.of(getTempCustomerMock()));

        List<TemporaryCustomerDTO> allTempCustomers = temporaryCustomerService.getAllTempCustomers();
        assertThat(allTempCustomers).hasSizeGreaterThan(0);
    }

    @Test
    void getTempCustomerById() {
        computeGetTempCustomerByIdMock(getTempCustomerMock());
        TemporaryCustomerDTO tempCustomerById = temporaryCustomerService.getTempCustomerById(1L);

        assertThat(tempCustomerById.fullName()).isEqualTo(FULL_NAME);
        assertThat(tempCustomerById.userName()).isEqualTo(USERNAME);
        assertThat(tempCustomerById.emailAddress()).isEqualTo(MAIL);
    }

    @Test
    void getTempCustomerByIdThrowsException() {
        computeGetTempCustomerByIdMock(null);
        assertThatThrownBy(() -> temporaryCustomerService.getTempCustomerById(1L))
                .isInstanceOf(TemporaryCustomerByIdException.class)
                .hasMessage("Temporary Customer with specific id cannot be found");
    }

    @Test
    void saveTempUser() {
        TemporaryCustomerRequest request = getTemporaryCustomerRequest();
        when(repository.save(any())).thenReturn(getTempCustomerMock());

        TemporaryCustomerIdResponse temporaryCustomerIdResponse = temporaryCustomerService.saveTempCustomer(request, 1L);
        verify(repository).save(any());
        assertThat(temporaryCustomerIdResponse).isNotNull();
        assertThat(temporaryCustomerIdResponse.id()).isNotNull();
    }

    @Test
    void assignOrderIdToTempCustomer() {
        TemporaryCustomer tempCustomerMock = getTempCustomerMock();
        long orderId = 10L;

        computeGetTempCustomerByIdMock(tempCustomerMock);

        temporaryCustomerService.assignOrderIdToTempCustomer(tempCustomerMock.getId(), new OrderIdRequest(orderId));
        verify(repository).save(any());
    }

    private void computeGetTempCustomerByIdMock(TemporaryCustomer temporaryCustomer) {
        when(repository.findById(any()))
                .thenReturn(
                       Optional.ofNullable(temporaryCustomer)
                );
    }

}
