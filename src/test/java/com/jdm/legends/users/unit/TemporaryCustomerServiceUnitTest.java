package com.jdm.legends.users.unit;

import com.jdm.legends.users.repository.TemporaryCustomerRepository;
import com.jdm.legends.users.service.TemporaryCustomerService;
import com.jdm.legends.users.service.dto.Car;
import com.jdm.legends.users.service.dto.HistoryBid;
import com.jdm.legends.users.service.entity.TemporaryCustomer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        when(repository.findAll()).thenReturn(getTempUsersMockData());

        List<TemporaryCustomer> allTempUsers = temporaryCustomerService.getAllTempUsers();

        verify(repository).findAll();
        assertThat(allTempUsers).isNotEmpty();
        assertEquals(getTempUsersMockData().size(), allTempUsers.size());
        assertThat(getTempUsersMockData()).hasSameSizeAs(allTempUsers);
    }

    @Test
    void saveTempUserSuccessfully() {
        temporaryCustomerService.saveUser(getTempUsersMockData().get(0), getHistoryBidMockData());
        verify(repository).save(any());
    }

    private List<TemporaryCustomer> getTempUsersMockData() {
        return List.of(
                TemporaryCustomer.builder().userName("tes12").fullName("John Mick").emailAddress("john12@gmail.com").historyBidList(new ArrayList<>()).build(),
                TemporaryCustomer.builder().userName("harry66").fullName("Harry Style").emailAddress("harrymusic@gmail.com").historyBidList(new ArrayList<>()).build(),
                TemporaryCustomer.builder().userName("therock").fullName("The Rock").emailAddress("sevenbucksprod@gmail.com").historyBidList(new ArrayList<>()).build()
        );
    }

    private HistoryBid getHistoryBidMockData() {
        return HistoryBid.builder()
                .bidValue(new BigDecimal("98098908954"))
                .temporaryUsersList(new HashSet<>(getTempUsersMockData()))
                .timeOfTheBid(LocalDateTime.MIN)
                .car(new Car())
                .build();
    }
}
