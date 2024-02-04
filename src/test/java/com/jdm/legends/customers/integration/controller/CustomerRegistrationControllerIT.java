package com.jdm.legends.customers.integration.controller;

import com.jdm.legends.customers.controller.dto.CustomerRequest;
import com.jdm.legends.customers.service.enums.RolesType;
import com.jdm.legends.customers.service.repository.CustomerRepository;
import com.jdm.legends.customers.utils.TestDummy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.jdm.legends.customers.utils.TestDummy.*;
import static com.jdm.legends.customers.utils.UtilsMock.readValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;

import static com.jdm.legends.customers.utils.UtilsMock.writeJsonAsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test-in-memory")
public class CustomerRegistrationControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void registerCustomerSuccessfully() throws Exception {
        CustomerRequest customerRequest = new CustomerRequest(
                FULL_NAME, USERNAME
                , List.of(RolesType.CLIENT, RolesType.POTENTIAL_CLIENT)
                , "4322343234", MAIL,
                "password");

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post("/register-customer")
                .contentType(APPLICATION_JSON)
                .content(writeJsonAsString(customerRequest))
                .accept(APPLICATION_JSON);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(header().exists("X-XSRF-TOKEN"));
    }

}
