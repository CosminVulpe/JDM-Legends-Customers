package com.jdm.legends.customers.controller.dto;

public record TemporaryCustomerRequest(
        String fullName,
        String userName,
        String emailAddress,
        String role,
        boolean checkInformationStoredTemporarily
) {
}
