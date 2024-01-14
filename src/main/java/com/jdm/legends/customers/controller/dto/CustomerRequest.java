package com.jdm.legends.customers.controller.dto;

import com.jdm.legends.customers.service.enums.Roles;

import javax.validation.constraints.Email;

public record CustomerRequest(
        String fullName,
        String userName,

        @Email
        String emailAddress,
        Roles role,
        String pwd
) {
}
