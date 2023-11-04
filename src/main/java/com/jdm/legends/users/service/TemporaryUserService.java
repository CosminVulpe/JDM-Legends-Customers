package com.jdm.legends.users.service;

import com.jdm.legends.users.repository.TemporaryUserRepository;
import com.jdm.legends.users.repository.WinnerUser;
import com.jdm.legends.users.service.dto.HistoryBid;
import com.jdm.legends.users.service.dto.TemporaryUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemporaryUserService {
    private final TemporaryUserRepository repository;

    public List<TemporaryUser> getAllTempUsers() {
        return repository.findAll();
    }

    public void saveUser(@Valid TemporaryUser temporaryUser, HistoryBid historyBid) {
        temporaryUser.addHistoryBid(historyBid);
        repository.save(temporaryUser);
        log.info("Successfully saved temporary user");
    }

    public Optional<WinnerUser> getWinnerUser(Long carId) {
        return repository.getWinnerUser(carId);
    }

}
