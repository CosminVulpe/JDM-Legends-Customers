package com.jdm.legends.customers.controller;

import com.jdm.legends.customers.controller.dto.CustomerIdResponse;
import com.jdm.legends.customers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/checkEmail")
    public boolean checkEmail(@Valid @NotBlank @RequestBody String email) {
        return customerService.checkEmail(email.replaceAll("^\"|\"$", ""));
    }

    @PostMapping("/getIdByEmail")
    public ResponseEntity<CustomerIdResponse> assignHistoryBid(@RequestBody String customerEmail) {
        return customerService.getIdByEmailAddress(customerEmail);
    }
}
