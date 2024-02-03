package com.jdm.legends.customers.utils;

import com.jdm.legends.customers.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.customers.service.entity.Customer;
import com.jdm.legends.customers.service.entity.ReminderEmail;
import com.jdm.legends.customers.service.entity.Role;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import com.jdm.legends.customers.service.enums.RolesType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.jdm.legends.customers.service.enums.RolesType.*;

@Service
public class TestDummy {

    public static final String FULL_NAME = "John Cena";
    public static final String USERNAME = "cannotseeme";
    public static final String MAIL = "JohnCeva@yahoo.com";

    private TestDummy() {
    }

    public static TemporaryCustomer getTempCustomerMock() {
        return TemporaryCustomer.builder()
                .emailAddress(MAIL)
                .id(1L)
                .fullName(FULL_NAME)
                .userName(USERNAME)
                .role("anonymous".toUpperCase())
                .checkInformationStoredTemporarily(true)
                .build();
    }

    public static TemporaryCustomerRequest getTemporaryCustomerRequest() {
        return new TemporaryCustomerRequest(FULL_NAME, USERNAME, MAIL, "Potential Client", true);
    }

    public static ReminderEmail getReminderEmailMock() {
        LocalDateTime now = LocalDateTime.now();
        return ReminderEmail.builder().sentTimeEmail(now).deadLineEmail(now.plusHours(24)).build();
    }

    public static Customer getCustomMock() {
        Role role = new Role(List.of(ADMIN, CLIENT, POTENTIAL_CLIENT));
        return Customer.builder()
                .emailAddress(MAIL)
                .phoneNumber("123123123123")
                .userName(USERNAME)
                .pwd("IloveGrandpa")
                .fullName(FULL_NAME)
                .role(role)
                .build();
    }
}
