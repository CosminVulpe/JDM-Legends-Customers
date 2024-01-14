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

    public static Roles getRole(String role) {
        return Arrays.stream(Roles.values()).filter(item -> item.name().equalsIgnoreCase(role) || item.value.equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new RolesNotFoundException("Unable to find " + role + " within the system"));
    }
}

