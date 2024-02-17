package com.jdm.legends.customers.integration.controller;

import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import com.jdm.legends.customers.service.repository.TemporaryCustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jdm.legends.customers.service.enums.RolesType.CLIENT;
import static com.jdm.legends.customers.service.enums.RolesType.POTENTIAL_CLIENT;
import static com.jdm.legends.customers.utils.TestDummy.*;
import static com.jdm.legends.customers.utils.UtilsMock.writeJsonAsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test-in-memory")
public class CustomerRegistrationControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TemporaryCustomerRepository temporaryCustomerRepository;

    private static final String X_XSRF_TOKEN = "X-XSRF-TOKEN";

    @Test
    void registerCustomerSuccessfully() throws Exception {
        CustomerRequest customerRequest = getCustomerRequest();

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post("/register-customer")
                .contentType(APPLICATION_JSON)
                .content(writeJsonAsString(customerRequest))
                .accept(APPLICATION_JSON);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(header().exists(X_XSRF_TOKEN));
    }

    @Test
    void registerCustomerFromPotentialClient() throws Exception {
        TemporaryCustomer tempCustomerMock = getTempCustomerMock();
        tempCustomerMock.setOrderId(1L);
        tempCustomerMock.setHistoryBidId("1");

        TemporaryCustomer temporaryCustomer = temporaryCustomerRepository.save(tempCustomerMock);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post("/register-customer")
                .param("tempCustomerId", String.valueOf(temporaryCustomer.getId()))
                .contentType(APPLICATION_JSON)
                .content(writeJsonAsString(getCustomerRequest()))
                .accept(APPLICATION_JSON);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(header().exists(X_XSRF_TOKEN));
    }

    private static CustomerRequest getCustomerRequest() {
        return new CustomerRequest(
                FULL_NAME, USERNAME
                , List.of(CLIENT, POTENTIAL_CLIENT)
                , "4322343234", MAIL,
                "password");
    }

}
