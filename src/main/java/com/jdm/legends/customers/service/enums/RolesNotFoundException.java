package com.jdm.legends.customers.service.enums;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@Slf4j
public final class RolesNotFoundException extends RuntimeException {
    public RolesNotFoundException(String message) {
        super(message);
        log.error(message);
    }
}
