package com.jdm.legends.customers.utils;

import com.jdm.legends.customers.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import org.springframework.stereotype.Service;

@Service
public class TestDummy {

    public static final String FULL_NAME = "John Cena";
    public static final String USERNAME = "cannotseeme";
    public static final String MAIL = "JohnCeva@yahoo.com";

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
}
