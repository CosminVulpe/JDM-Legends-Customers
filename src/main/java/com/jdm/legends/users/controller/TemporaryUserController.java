package com.jdm.legends.users.controller;

import com.jdm.legends.users.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.users.controller.dto.TemporaryCustomerResponse;
import com.jdm.legends.users.repository.WinnerUser;
import com.jdm.legends.users.service.TemporaryCustomerService;
import com.jdm.legends.users.service.entity.TemporaryCustomer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/temporary-customer")
public class TemporaryUserController {
    private final TemporaryCustomerService service;

    @PostMapping(path = "/save/{historyBidId}")
    public TemporaryCustomerResponse saveTempUser(@RequestBody TemporaryCustomerRequest request, @PathVariable Long historyBidId){
       return service.saveUser(request ,historyBidId);
    }

    @GetMapping()
    public List<TemporaryCustomer> getAllTempUsers() {
        return service.getAllTempUsers();
    }

    @GetMapping(path = "/winner/{carId}")
    public Optional<WinnerUser> getWinner(@PathVariable Long carId){
        return service.getWinnerUser(carId);
    }
}
