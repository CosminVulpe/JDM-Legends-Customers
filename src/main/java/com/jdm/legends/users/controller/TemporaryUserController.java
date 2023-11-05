package com.jdm.legends.users.controller;

import com.jdm.legends.users.repository.WinnerUser;
import com.jdm.legends.users.service.TemporaryUserService;
import com.jdm.legends.users.service.dto.HistoryBidTemporaryUser;
import com.jdm.legends.users.service.dto.TemporaryUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/temporary-user")
public class TemporaryUserController {
    private final TemporaryUserService service;

    @PostMapping(path = "/save")
    public void saveTempUser(@RequestBody HistoryBidTemporaryUser historyBidTemporaryUser) {
        service.saveUser(historyBidTemporaryUser.getTemporaryUser(), historyBidTemporaryUser.getHistoryBid());
    }

    @GetMapping()
    public List<TemporaryUser> getAllTempUsers() {
        return service.getAllTempUsers();
    }

    @GetMapping(path = "/winner/{carId}")
    public Optional<WinnerUser> getWinner(@PathVariable Long carId){
        return service.getWinnerUser(carId);
    }
}
