package com.jdm.legends.customers.controller.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record CustomerRequest(
        @NotBlank
        String fullName,

        @NotBlank
        String userName,

        @Email
        String emailAddress,

        @NotBlank
        String pwd
) {
}
