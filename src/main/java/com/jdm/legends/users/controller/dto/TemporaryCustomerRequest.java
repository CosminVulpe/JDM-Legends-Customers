package com.jdm.legends.users.controller.dto;

public record TemporaryCustomerRequest(
        String fullName,
        String userName,
        String emailAddress,
        String role,
        boolean checkInformationStoredTemporarily
) {
}
