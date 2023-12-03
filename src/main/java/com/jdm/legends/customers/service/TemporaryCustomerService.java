package com.jdm.legends.customers.service;

import com.jdm.legends.customers.controller.dto.TemporaryCustomerDTO;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerIdResponse;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.customers.controller.dto.WinnerCustomerResponse;
import com.jdm.legends.customers.repository.TemporaryCustomerRepository;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import com.jdm.legends.customers.service.mapping.TemporaryCustomerMapper;
import com.jdm.legends.customers.service.repository.TemporaryCustomerRepo;
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

    public List<TemporaryCustomerDTO> getAllTempCustomers() {
        return repository.findAll().stream().map(TemporaryCustomerMapper.INSTANCE::tempCustomerToTempCustomerDTO).toList();
    }

    public TemporaryCustomerDTO getTempCustomerById(Long id) {
        TemporaryCustomer temporaryCustomer = repository.findById(id).orElseThrow(() -> new TemporaryCustomerByIdException("Temporary Customer with specific id cannot be found"));
        return TemporaryCustomerMapper.INSTANCE.tempCustomerToTempCustomerDTO(temporaryCustomer);
    }

    public TemporaryCustomerIdResponse saveTempCustomer(TemporaryCustomerRequest request, Long historyBidId) {
        TemporaryCustomer temporaryCustomer = TemporaryCustomerMapper.INSTANCE.tempCustomerRequestToTempCustomerEntity(request);
        temporaryCustomer.setHistoryBidId(historyBidId);

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
            log.error(message);
        }
    }

}
