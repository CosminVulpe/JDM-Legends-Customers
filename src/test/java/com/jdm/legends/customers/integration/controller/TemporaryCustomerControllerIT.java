package com.jdm.legends.customers.integration.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jdm.legends.customers.controller.dto.OrderIdRequest;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerDTO;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerIdResponse;
import com.jdm.legends.customers.controller.dto.WinnerCustomerResponse;
import com.jdm.legends.customers.repository.TemporaryCustomerRepository;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jdm.legends.customers.utils.TestDummy.getTemporaryCustomerRequest;
import static com.jdm.legends.customers.utils.UtilsMock.readValue;
import static com.jdm.legends.customers.utils.UtilsMock.readValueResponseEntity;
import static com.jdm.legends.customers.utils.UtilsMock.writeJsonAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test-in-memory")
@Sql("/add-temporary-customers.sql")
public class TemporaryCustomerControllerIT {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TemporaryCustomerRepository repository;

    private TemporaryCustomer temporaryCustomer;

    @BeforeEach
    void setUp() {
        temporaryCustomer = repository.findAll().get(0);
    }

    private static final String temporaryCustomerRequestMapping = "/temporary-customer";

    @Test
    void saveTemporaryCustomer() throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post(temporaryCustomerRequestMapping + "/save/{historyBidId}", 1L)
                .contentType(APPLICATION_JSON)
                .content(writeJsonAsString(getTemporaryCustomerRequest()))
                .accept(APPLICATION_JSON);

        MvcResult mvcResult = mvc.perform(builder)
                .andExpect(status().isOk())
                .andReturn();
        TemporaryCustomerIdResponse temporaryCustomerIdResponse = readValue(mvcResult.getResponse().getContentAsString(), TemporaryCustomerIdResponse.class);
        assertThat(temporaryCustomerIdResponse).isNotNull();
        assertThat(temporaryCustomerIdResponse.id()).isNotNull();
    }

    @Test
    void getAllTemporaryCustomers() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(temporaryCustomerRequestMapping).accept(APPLICATION_JSON);

        String contentAsString = mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        List<TemporaryCustomer> temporaryCustomers = readValue(contentAsString, new TypeReference<>() {
        });

        assertThat(temporaryCustomers).isNotEmpty();
        assertThat(temporaryCustomers).hasSize(3);
    }

    @Test
    void getTempCustomerById() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(temporaryCustomerRequestMapping + "/{temporaryCustomerId}", temporaryCustomer.getId())
                .accept(APPLICATION_JSON);

        String contentAsString = mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        TemporaryCustomerDTO temporaryCustomerDTO = readValue(contentAsString, TemporaryCustomerDTO.class);

        assertThat(temporaryCustomerDTO).isNotNull();
        assertThat(temporaryCustomerDTO.userName()).isNotNull();
        assertThat(temporaryCustomerDTO.fullName()).isNotNull();
        assertThat(temporaryCustomerDTO.role()).isNotNull();
    }

    @Test
    void assignOrderIdToTempCustomer() throws Exception {
        long orderId = 10L;
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(temporaryCustomerRequestMapping + "/assign/{tempCustomerId}", temporaryCustomer.getId())
                .accept(APPLICATION_JSON)
                .content(writeJsonAsString(new OrderIdRequest(orderId)))
                .contentType(APPLICATION_JSON);

        mvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(temporaryCustomer.getOrderId()).isNotNull();
        assertThat(temporaryCustomer.getOrderId()).isEqualTo(orderId);
    }

}
