package com.jdm.legends.users.unit;

import com.jdm.legends.users.repository.TemporaryUserRepository;
import com.jdm.legends.users.service.TemporaryUserService;
import com.jdm.legends.users.service.dto.Car;
import com.jdm.legends.users.service.dto.HistoryBid;
import com.jdm.legends.users.service.dto.TemporaryUser;
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
class TemporaryUserServiceUnitTest {

    @Mock
    private TemporaryUserRepository repository;

    @InjectMocks
    private TemporaryUserService temporaryUserService;

    @Test
    void getAllTempUsersSuccessfully() {
        when(repository.findAll()).thenReturn(getTempUsersMockData());

        List<TemporaryUser> allTempUsers = temporaryUserService.getAllTempUsers();

        verify(repository).findAll();
        assertThat(allTempUsers).isNotEmpty();
        assertEquals(getTempUsersMockData().size(), allTempUsers.size());
        assertThat(getTempUsersMockData()).hasSameSizeAs(allTempUsers);
    }

    @Test
    void saveTempUserSuccessfully() {
        temporaryUserService.saveUser(getTempUsersMockData().get(0), getHistoryBidMockData());
        verify(repository).save(any());
    }

    private List<TemporaryUser> getTempUsersMockData() {
        return List.of(
                TemporaryUser.builder().userName("tes12").fullName("John Mick").emailAddress("john12@gmail.com").historyBidList(new ArrayList<>()).build(),
                TemporaryUser.builder().userName("harry66").fullName("Harry Style").emailAddress("harrymusic@gmail.com").historyBidList(new ArrayList<>()).build(),
                TemporaryUser.builder().userName("therock").fullName("The Rock").emailAddress("sevenbucksprod@gmail.com").historyBidList(new ArrayList<>()).build()
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
