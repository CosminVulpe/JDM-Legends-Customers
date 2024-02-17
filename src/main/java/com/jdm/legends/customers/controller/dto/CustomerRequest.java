package com.jdm.legends.customers.controller.dto;

import com.jdm.legends.customers.service.enums.RolesType;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

public record CustomerRequest(
        @Nullable
        String fullName,
        @Nullable
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
