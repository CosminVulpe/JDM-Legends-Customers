package com.jdm.legends.customers.controller.dto;

import com.jdm.legends.customers.service.enums.RolesType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public record CustomerRequest(
        @NotBlank
        String fullName,
        @NotBlank
        String userName,

        List<RolesType> rolesTypes,

        @NotBlank
        String phoneNumber,

        @Email
        String emailAddress,

        @NotBlank
        String pwd
) {
}
