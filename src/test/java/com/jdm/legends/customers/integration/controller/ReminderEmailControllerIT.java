package com.jdm.legends.customers.integration.controller;

import com.jdm.legends.customers.repository.ReminderEmailRepository;
import com.jdm.legends.customers.repository.TemporaryCustomerRepository;
import com.jdm.legends.customers.service.entity.ReminderEmail;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import com.jdm.legends.customers.utils.TestDummy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test-in-memory")
@Sql("/add-temporary-customers.sql")
public class ReminderEmailControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderEmailRepository reminderEmailRepository;

    @Autowired
    private TemporaryCustomerRepository temporaryCustomerRepository;

    @BeforeEach
    void setUp() {
        reminderEmailRepository.save(TestDummy.getReminderEmailMock());
    }

    @Test
    void setEmailEnterDateWhenDeadlineIsNotPassed() throws Exception {
        TemporaryCustomer temporaryCustomer = temporaryCustomerRepository.findAll().get(0);
        ReminderEmail reminderEmail = reminderEmailRepository.findAll().get(0);
        reminderEmail.setTemporaryCustomerId(temporaryCustomer.getId());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/reminder-email")
                .param("tempCustomerId", String.valueOf(temporaryCustomer.getId()));

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect(status().isOk());
        assertThat(reminderEmail.getEnterTimeEmail()).isNotNull();
    }

    @Test
    void checkEmailEnterDateWhenDeadlineIsPassed() throws Exception {
        TemporaryCustomer temporaryCustomer = temporaryCustomerRepository.findAll().get(0);
        ReminderEmail reminderEmail = reminderEmailRepository.findAll().get(0);
        reminderEmail.setDeadLineEmail(now().minusYears(6));
        reminderEmail.setTemporaryCustomerId(temporaryCustomer.getId());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/reminder-email")
                .param("tempCustomerId", String.valueOf(temporaryCustomer.getId()));

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect(status().isOk());

        assertThat(reminderEmail.getEnterTimeEmail()).isNull();
    }
}
