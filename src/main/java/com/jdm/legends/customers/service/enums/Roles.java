package com.jdm.legends.customers.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Roles {
    POTENTIAL_CLIENT("Potential Client"),
    CLIENT("Client"),
    ANONYMOUS("Anonymous"),
    ADMIN("Admin");

    private final String value;

}

