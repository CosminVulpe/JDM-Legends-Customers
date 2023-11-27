package com.jdm.legends.customers.service.repository;

import com.jdm.legends.customers.controller.dto.WinnerCustomerResponse;
import com.jdm.legends.customers.repository.TemporaryCustomerRepository;
import com.jdm.legends.customers.service.TemporaryCustomerService.WinnerCustomerException;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class TemporaryCustomerRepo {
    private final TemporaryCustomerRepository repository;
    private final RestTemplate restTemplate;
    private final int dealershipCarsServerPort;
    private final String serverHost;

    public TemporaryCustomerRepo(TemporaryCustomerRepository repository, RestTemplate restTemplate, @Value("${jdm-legends-dealership-cars-port}") int dealershipCarsServerPort, @Value("${server.host}") String serverHost) {
        this.repository = repository;
        this.restTemplate = restTemplate;
        this.dealershipCarsServerPort = dealershipCarsServerPort;
        this.serverHost = serverHost;
    }

    public ResponseEntity<WinnerCustomerResponse> getWinnerUser(Long carId) {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(serverHost + dealershipCarsServerPort + "/car/max/bidValue/{carId}").buildAndExpand(carId);
        ResponseEntity<WinnerCustomerResponse> restTemplateForEntity = restTemplate.getForEntity(uriComponents.toUriString(), WinnerCustomerResponse.class);

        if (!restTemplateForEntity.getStatusCode().is2xxSuccessful()) {
            String msgError = "Unable to get winner max bid value";
            log.error(msgError);
            throw new WinnerCustomerException(msgError);
        }

        WinnerCustomerResponse response = restTemplateForEntity.getBody();
        if (isNull(response)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        TemporaryCustomer tempCustomer = repository.findAll().stream().filter(temporaryCustomer -> temporaryCustomer.getHistoryBidId().equals(response.historyBidId())).findFirst().orElseThrow();
        WinnerCustomerResponse winnerCustomerResponse = new WinnerCustomerResponse(response.bidValue(), response.historyBidId(), tempCustomer.getUserName(), tempCustomer.getEmailAddress());
        return ResponseEntity.ok(winnerCustomerResponse);
    }

}
