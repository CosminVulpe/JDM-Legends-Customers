package com.jdm.legends.users.service;

import com.jdm.legends.users.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.users.controller.dto.TemporaryCustomerResponse;
import com.jdm.legends.users.repository.TemporaryCustomerRepository;
import com.jdm.legends.users.repository.WinnerUser;
import com.jdm.legends.users.service.entity.TemporaryCustomer;
import com.jdm.legends.users.service.enums.Roles;
import com.jdm.legends.users.service.mapping.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public TemporaryCustomerResponse saveUser(TemporaryCustomerRequest request, Long historyBidId) {
        Mapper<TemporaryCustomerRequest, TemporaryCustomer> mapperCustomer = (TemporaryCustomerRequest source) ->
                TemporaryCustomer.builder()
                        .fullName(source.fullName())
                        .userName(source.userName())
                        .emailAddress(source.emailAddress())
                        .role(source.role())
                        .checkInformationStoredTemporarily(source.checkInformationStoredTemporarily())
                        .role( (source.checkInformationStoredTemporarily()) ? Roles.POTENTIAL_CLIENT.getValue() : Roles.ANONYMOUS.getValue())
                        .historyBidId(historyBidId)
                        .build();
        TemporaryCustomer temporaryCustomer = mapperCustomer.map(request);
        TemporaryCustomer temporaryCustomerSaved = repository.save(temporaryCustomer);
        return new TemporaryCustomerResponse(temporaryCustomerSaved.getId());
    }

    public Optional<WinnerUser> getWinnerUser(Long carId) {
        return repository.getWinnerUser(carId);
    }

}
