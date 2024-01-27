package com.jdm.legends.customers.controller.dto;

import javax.validation.constraints.NotBlank;

public record CustomerSignRequest(
        @NotBlank
        String emailAddress,

        @NotBlank
        String pwd) {
}
