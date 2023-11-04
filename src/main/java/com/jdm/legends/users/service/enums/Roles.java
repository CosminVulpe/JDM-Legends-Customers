package com.jdm.legends.users.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Roles {
    POTENTIAL_CLIENT("Potential Client"),
    ANONYMOUS("Anonymous"),
    ADMIN("Admin");

    @Getter
    private final String value;
}
