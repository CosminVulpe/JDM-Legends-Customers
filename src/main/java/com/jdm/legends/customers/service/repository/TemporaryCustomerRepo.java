package com.jdm.legends.customers.service.repository;

import com.jdm.legends.customers.controller.dto.WinnerCustomerResponse;
import com.jdm.legends.customers.repository.TemporaryCustomerRepository;
import com.jdm.legends.customers.service.entity.TemporaryCustomer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

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
        try {
            UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(serverHost + dealershipCarsServerPort + "/car/max/bidValue/{carId}").buildAndExpand(carId);
            ResponseEntity<WinnerCustomerResponse> restTemplateForEntity = restTemplate.getForEntity(uriComponents.toUriString(), WinnerCustomerResponse.class);

            log.info("Response status {} endpoint {}", restTemplateForEntity.getStatusCodeValue(), uriComponents);
            if (!restTemplateForEntity.getStatusCode().is2xxSuccessful()) {
                throw new ResponseStatusException(restTemplateForEntity.getStatusCode(), restTemplateForEntity.toString());
            }

            WinnerCustomerResponse response = restTemplateForEntity.getBody();
            log.info("Get response {} from dealership-cars on route {}", response, uriComponents);
            if (isNull(response)) {
                return ResponseEntity.status(NO_CONTENT).build();
            }

            TemporaryCustomer tempCustomer = repository.findAll().stream().filter(temporaryCustomer -> temporaryCustomer.getHistoryBidId().equals(response.historyBidId())).findFirst().orElseThrow();
            WinnerCustomerResponse winnerCustomerResponse = new WinnerCustomerResponse(response.bidValue(), response.historyBidId(), tempCustomer.getUserName(), tempCustomer.getEmailAddress(), tempCustomer.getId());
            log.info("Selecting the winner with status {} ", OK.value());
            return ResponseEntity.ok(winnerCustomerResponse);

        } catch (RestClientException e) {
            String msgError = "Unable to get winner max bid value";
            log.error(msgError);
            throw new RestClientException(msgError);
        }
    }

}
