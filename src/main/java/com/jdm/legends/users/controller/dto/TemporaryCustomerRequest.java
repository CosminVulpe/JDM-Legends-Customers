package com.jdm.legends.users.controller.dto;

import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;

public record TemporaryCustomerRequest(

        @Nullable
        String fullName,

        @Nullable
        String userName,

        @Email
        String emailAddress,

        @Nullable
        String role,
        boolean checkInformationStoredTemporarily
) {
}
