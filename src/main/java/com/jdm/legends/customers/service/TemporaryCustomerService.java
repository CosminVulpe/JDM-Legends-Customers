package com.jdm.legends.customers.service;

import com.jdm.legends.customers.controller.dto.OrderIdRequest;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerDTO;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerIdResponse;
import com.jdm.legends.customers.controller.dto.TemporaryCustomerRequest;
import com.jdm.legends.customers.controller.dto.WinnerCustomerResponse;
import com.jdm.legends.customers.service.repository.TemporaryCustomerRepository;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import com.jdm.legends.customers.service.mapping.TemporaryCustomerMapper;
import com.jdm.legends.customers.service.repository.DealershipCarsRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemporaryCustomerService {
    private final TemporaryCustomerRepository repository;
    private final DealershipCarsRepo dealershipCarsRepo;

    private static final String DELIMITER = ", ";

    public List<TemporaryCustomerDTO> getAllTempCustomers() {
        return repository.findAll().stream().map(TemporaryCustomerMapper.INSTANCE::tempCustomerToTempCustomerDTO).toList();
    }

    public TemporaryCustomerDTO getTempCustomerById(Long id) {
        return TemporaryCustomerMapper.INSTANCE.tempCustomerToTempCustomerDTO(getTemporaryCustomerByIdOrElseThrow(id));
    }

    public List<TemporaryCustomerDTO> getAllTempCustomerByHistoryBidId(String historyBidId) {
        List<TemporaryCustomer> temporaryCustomers = repository.findAll().stream()
                .filter(item -> item.doesHistoryBidExists(historyBidId)).toList();

        return temporaryCustomers.stream().map(TemporaryCustomerMapper.INSTANCE::tempCustomerToTempCustomerDTO).toList();
    }

    public TemporaryCustomerIdResponse saveTempCustomer(TemporaryCustomerRequest request, Long historyBidId) {
        Optional<TemporaryCustomer> tempCustomerByEmailOrUsername = findTempCustomerByEmailOrUsername(request);
        TemporaryCustomer temporaryCustomer;

        if (tempCustomerByEmailOrUsername.isPresent()) {
            temporaryCustomer = tempCustomerByEmailOrUsername.get();
            temporaryCustomer.setHistoryBidId(temporaryCustomer.getHistoryBidId() + DELIMITER + historyBidId);
        } else {
            temporaryCustomer = TemporaryCustomerMapper.INSTANCE.tempCustomerRequestToTempCustomerEntity(request);
            temporaryCustomer.setHistoryBidId(String.valueOf(historyBidId));
        }

        TemporaryCustomer temporaryCustomerSaved = repository.save(temporaryCustomer);
        return new TemporaryCustomerIdResponse(temporaryCustomerSaved.getId());
    }

    public ResponseEntity<WinnerCustomerResponse> selectWinnerCustomer(Long carId) {
        return dealershipCarsRepo.selectWinnerCustomer(carId);
    }

    public void assignOrderIdToTempCustomer(Long tempCustomerId, OrderIdRequest request) {
        TemporaryCustomer temporaryCustomerById = getTemporaryCustomerByIdOrElseThrow(tempCustomerId);
        Long orderId = request.orderId();
        temporaryCustomerById.setOrderId(orderId);
        repository.save(temporaryCustomerById);
    }

    private TemporaryCustomer getTemporaryCustomerByIdOrElseThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new TemporaryCustomerByIdException("Temporary Customer with specific id cannot be found"));
    }

    private Optional<TemporaryCustomer> findTempCustomerByEmailOrUsername(TemporaryCustomerRequest request) {
        return repository.findAll().stream()
                .filter(item -> item.getUserName().equalsIgnoreCase(request.userName())
                        || item.getEmailAddress().equalsIgnoreCase(request.emailAddress()))
                .findFirst();
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
