package com.jdm.legends.users.integration;

import com.jdm.legends.users.repository.TemporaryUserRepository;
import com.jdm.legends.users.service.dto.Car;
import com.jdm.legends.users.service.dto.HistoryBid;
import com.jdm.legends.users.service.dto.HistoryBidTemporaryUser;
import com.jdm.legends.users.service.dto.TemporaryUser;
import com.jdm.legends.users.utils.UtilsMock;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.jdm.legends.users.utils.UtilsMock.buildCarRequest;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test-in-memory")
class TemporaryUserServiceIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TemporaryUserRepository repository;

    @Test
    void shouldSaveTempUserSuccessfully() throws Exception {
        Car car = buildCarRequest();
        HistoryBid historyBid = car.getHistoryBidList().get(0);
        TemporaryUser temporaryUser = historyBid.getTemporaryUsersList().stream().findFirst().orElse(new TemporaryUser());

        HistoryBidTemporaryUser request = HistoryBidTemporaryUser.builder()
                .temporaryUser(temporaryUser)
                .historyBid(historyBid)
                .build();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/temporary-user/save")
                .contentType(APPLICATION_JSON)
                .content(UtilsMock.writeJsonAsString(request))
                .accept(APPLICATION_JSON);

        mvc.perform(builder).andExpect(status().isOk());
    }

    @Test
    void shouldGetAllTempUsers() throws Exception {
        Car car = buildCarRequest();
        HistoryBid historyBid = car.getHistoryBidList().get(0);
        TemporaryUser temporaryUser = historyBid.getTemporaryUsersList().stream().findFirst().orElse(new TemporaryUser());

        repository.save(temporaryUser);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/temporary-user").accept(APPLICATION_JSON);

        mvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].fullName").value("John Cena"))
                .andExpect(jsonPath("$.[0].userName").value("cannot_see_me98"))
                .andExpect(jsonPath("$.[0].emailAddress").value("johnCeva12@yahoo.com"));
    }

    @Test
    @Disabled("Under development")
    void shouldGetWinnerCarBid() throws Exception {
        Car car = buildCarRequest();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/temporary-user/winner/{carId}", car.getId())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
        mvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print());
    }
}
