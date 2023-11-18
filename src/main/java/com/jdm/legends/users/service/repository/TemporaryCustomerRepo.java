package com.jdm.legends.users.service.repository;

import com.jdm.legends.users.controller.dto.WinnerCustomerResponse;
import com.jdm.legends.users.repository.TemporaryCustomerRepository;
import com.jdm.legends.users.service.TemporaryCustomerService;
import com.jdm.legends.users.service.entity.TemporaryCustomer;
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
            throw new TemporaryCustomerService.WinnerCustomerException(msgError);
        }
        WinnerCustomerResponse body = restTemplateForEntity.getBody();
        if (isNull(body)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        TemporaryCustomer tempCustomer = repository.findAll().stream().filter(temporaryCustomer -> temporaryCustomer.getHistoryBidId().equals(body.historyBidId())).findFirst().orElseThrow();
        WinnerCustomerResponse winnerCustomerResponse = new WinnerCustomerResponse(body.bidValue(), body.historyBidId(), tempCustomer.getUserName(), tempCustomer.getEmailAddress());
        return ResponseEntity.ok(winnerCustomerResponse);
    }

}
