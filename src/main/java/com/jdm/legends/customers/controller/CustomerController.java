package com.jdm.legends.customers.controller;

import com.jdm.legends.customers.controller.dto.CustomerDTO;
import com.jdm.legends.customers.controller.dto.CustomerIdResponse;
import com.jdm.legends.customers.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/checkEmail")
    public boolean checkEmail(@Valid @NotBlank @RequestBody String email) {
        return customerService.checkEmail(email.replaceAll("^\"|\"$", ""));
    }

    @PostMapping("/assign/{historyBidId}")
    public ResponseEntity<CustomerIdResponse> assignHistoryBid(@RequestBody String customerEmail, @PathVariable String historyBidId) {
        return customerService.assignHistoryBid(customerEmail, historyBidId);
    }

    @GetMapping("/getHistoryBids/{historyBid}")
    public Optional<CustomerDTO> getHistoryBids(Authentication authentication, @PathVariable Long historyBid) {
        return customerService.getHistoryBid(authentication, historyBid);
    }
}
