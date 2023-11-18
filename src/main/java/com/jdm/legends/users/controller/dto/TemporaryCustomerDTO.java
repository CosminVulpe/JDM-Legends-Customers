package com.jdm.legends.users.controller.dto;

public record TemporaryCustomerDTO(
        Long id,
        String fullName,
        String userName,
        String emailAddress,
        String role,
        boolean checkInformationStoredTemporarily) {
}
