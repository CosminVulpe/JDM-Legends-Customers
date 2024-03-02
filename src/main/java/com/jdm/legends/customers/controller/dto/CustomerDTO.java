package com.jdm.legends.customers.controller.dto;

public record CustomerDTO(Long id,
                          String fullName,
                          String userName,
                          String emailAddress) {
}
