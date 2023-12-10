package com.jdm.legends.customers.controller;

import com.jdm.legends.customers.controller.dto.OrderIdRequest;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerDTO;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerIdResponse;
import com.jdm.legends.customers.controller.dto.WinnerCustomerResponse;
import com.jdm.legends.customers.service.TemporaryCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/temporary-customer")
public class TemporaryCustomerController {
    private final TemporaryCustomerService service;

    @PostMapping(path = "/save/{historyBidId}")
    public TemporaryCustomerIdResponse saveTempCustomer(@RequestBody TemporaryCustomerRequest request, @PathVariable Long historyBidId){
       return service.saveTempCustomer(request ,historyBidId);
    }

    @GetMapping
    public List<TemporaryCustomerDTO> getAllTempCustomer() {
        return service.getAllTempCustomers();
    }

    @GetMapping("get/history/{historyBidId}")
    public List<TemporaryCustomerDTO> getAllTempCustomerByHistoryBidId(@PathVariable String historyBidId) {
        return service.getAllTempCustomerByHistoryBidId(historyBidId);
    }

    @GetMapping(path = "/{temporaryCustomerId}")
    public TemporaryCustomerDTO getTempCustomerById(@PathVariable("temporaryCustomerId") Long temporaryCustomerId) {
        return service.getTempCustomerById(temporaryCustomerId);
    }

    @GetMapping(path = "/winner/{carId}")
    public ResponseEntity<WinnerCustomerResponse> getWinner(@PathVariable Long carId){
        return service.getWinnerUser(carId);
    }

    @PostMapping(path = "/assign/{tempCustomerId}")
    public void assignOrderIdToTempCustomer(@PathVariable Long tempCustomerId, @RequestBody OrderIdRequest request) {
        service.assignOrderIdToTempCustomer(tempCustomerId, request);
    }
}
