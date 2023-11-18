package com.jdm.legends.users.controller;

import com.jdm.legends.users.controller.dto.TemporaryCustomerDTO;
import com.jdm.legends.users.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.users.controller.dto.TemporaryCustomerIdResponse;
import com.jdm.legends.users.controller.dto.WinnerCustomerResponse;
import com.jdm.legends.users.service.TemporaryCustomerService;
import com.jdm.legends.users.service.entity.TemporaryCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/temporary-customer")
public class TemporaryUserController {
    private final TemporaryCustomerService service;

    @PostMapping(path = "/save/{historyBidId}")
    public TemporaryCustomerIdResponse saveTempUser(@RequestBody TemporaryCustomerRequest request, @PathVariable Long historyBidId){
       return service.saveTempCustomer(request ,historyBidId);
    }

    @GetMapping()
    public List<TemporaryCustomer> getAllTempUsers() {
        return service.getAllTempCustomers();
    }

    @GetMapping(path = "/{temporaryCustomerId}")
    public TemporaryCustomerDTO getCarById(@PathVariable("temporaryCustomerId") Long temporaryCustomerId) {
        return service.getTempCustomerById(temporaryCustomerId);
    }

    @GetMapping(path = "/winner/{carId}")
    public ResponseEntity<WinnerCustomerResponse> getWinner(@PathVariable Long carId){
        return service.getWinnerUser(carId);
    }
}
