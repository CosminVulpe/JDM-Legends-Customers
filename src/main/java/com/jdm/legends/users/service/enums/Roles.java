package com.jdm.legends.users.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Roles {
    POTENTIAL_CLIENT("Potential Client"),
    ANONYMOUS("Anonymous"),
    ADMIN("Admin");

    private final String value;
}
