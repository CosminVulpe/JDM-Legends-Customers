package com.jdm.legends.users.service;

import com.jdm.legends.users.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.users.repository.TemporaryCustomerRepository;
import com.jdm.legends.users.repository.WinnerUser;
import com.jdm.legends.users.service.dto.HistoryBid;
import com.jdm.legends.users.service.entity.TemporaryCustomer;
import com.jdm.legends.users.service.enums.Roles;
import com.jdm.legends.users.service.mapping.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemporaryCustomerService {
    private final TemporaryCustomerRepository repository;

    public List<TemporaryCustomer> getAllTempUsers() {
        return repository.findAll();
    }

    public void saveUser(@Valid TemporaryCustomerRequest temporaryCustomerRequest, HistoryBid historyBid) {
        Mapper<TemporaryCustomerRequest, TemporaryCustomer> mapper = (TemporaryCustomerRequest request) ->
                TemporaryCustomer.builder()
                        .fullName(request.fullName())
                        .userName(request.userName())
                        .emailAddress(request.emailAddress())
                        .role(request.role())
                        .checkInformationStoredTemporarily(request.checkInformationStoredTemporarily())
                        .historyBidList(new ArrayList<>())
                        .role( (request.checkInformationStoredTemporarily()) ? Roles.POTENTIAL_CLIENT.getValue() : Roles.ANONYMOUS.getValue())
                        .build();
        TemporaryCustomer temporaryCustomer = mapper.map(temporaryCustomerRequest);
        temporaryCustomer.addHistoryBid(historyBid);

        repository.save(temporaryCustomer);
        log.info("Successfully saved temporary customer");
    }

    public Optional<WinnerUser> getWinnerUser(Long carId) {
        return repository.getWinnerUser(carId);
    }

}
