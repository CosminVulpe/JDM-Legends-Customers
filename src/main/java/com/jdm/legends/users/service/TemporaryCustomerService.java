package com.jdm.legends.users.service;

import com.jdm.legends.users.controller.dto.TemporaryCustomerDTO;
import com.jdm.legends.users.controller.dto.TemporaryCustomerIdResponse;
import com.jdm.legends.users.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.users.controller.dto.WinnerCustomerResponse;
import com.jdm.legends.users.repository.TemporaryCustomerRepository;
import com.jdm.legends.users.service.entity.TemporaryCustomer;
import com.jdm.legends.users.service.enums.Roles;
import com.jdm.legends.users.service.mapping.Mapper;
import com.jdm.legends.users.service.repository.TemporaryCustomerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemporaryCustomerService {
    private final TemporaryCustomerRepository repository;
    private final TemporaryCustomerRepo temporaryCustomerRepo;

    public List<TemporaryCustomer> getAllTempCustomers() {
        return repository.findAll();
    }

    public TemporaryCustomerDTO getTempCustomerById(Long id) {
        TemporaryCustomer temporaryCustomer = repository.findById(id).orElseThrow(() -> new TemporaryCustomerByIdException("Temporary Customer with specific id cannot be found"));
        Mapper<TemporaryCustomer, TemporaryCustomerDTO> mapper = (TemporaryCustomer source) -> new TemporaryCustomerDTO(
                source.getId(), source.getFullName(),
                source.getUserName(), source.getEmailAddress(),
                source.getRole(), source.isCheckInformationStoredTemporarily());

        return mapper.map(temporaryCustomer);
    }

    public TemporaryCustomerIdResponse saveTempCustomer(TemporaryCustomerRequest request, Long historyBidId) {
        Mapper<TemporaryCustomerRequest, TemporaryCustomer> mapperCustomer = (TemporaryCustomerRequest source) ->
                TemporaryCustomer.builder()
                        .fullName(source.fullName())
                        .userName(source.userName())
                        .emailAddress(source.emailAddress())
                        .role(source.role())
                        .checkInformationStoredTemporarily(source.checkInformationStoredTemporarily())
                        .role((source.checkInformationStoredTemporarily()) ? Roles.POTENTIAL_CLIENT.getValue() : Roles.ANONYMOUS.getValue())
                        .historyBidId(historyBidId)
                        .build();
        TemporaryCustomer temporaryCustomer = mapperCustomer.map(request);
        TemporaryCustomer temporaryCustomerSaved = repository.save(temporaryCustomer);
        return new TemporaryCustomerIdResponse(temporaryCustomerSaved.getId());
    }

    public ResponseEntity<WinnerCustomerResponse> getWinnerUser(Long carId) {
        return temporaryCustomerRepo.getWinnerUser(carId);
    }

    @Slf4j
    @ResponseStatus(code = NOT_FOUND)
    public static final class TemporaryCustomerByIdException extends RuntimeException {
        public TemporaryCustomerByIdException(String message) {
            super(message);
            log.warn(message);
        }
    }

    @Slf4j
    @ResponseStatus(code = INTERNAL_SERVER_ERROR)
    public static final class WinnerCustomerException extends RuntimeException {
        public WinnerCustomerException(String message) {
            super(message);
            log.warn(message);
        }
    }

}
