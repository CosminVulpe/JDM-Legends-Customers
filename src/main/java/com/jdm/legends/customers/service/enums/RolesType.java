package com.jdm.legends.customers.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RolesType {
    POTENTIAL_CLIENT("Potential Client"),
    CLIENT("Client"),
    ANONYMOUS("Anonymous"),
    ADMIN("Admin");

    private final String value;

}

