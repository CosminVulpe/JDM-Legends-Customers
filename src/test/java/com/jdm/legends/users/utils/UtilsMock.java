package com.jdm.legends.users.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jdm.legends.users.service.dto.Car;
import com.jdm.legends.users.service.dto.HistoryBid;
import com.jdm.legends.users.service.dto.TemporaryUser;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.jdm.legends.users.service.enums.CarColor.BLACK;
import static com.jdm.legends.users.service.enums.CarCompany.TOYOTA;
import static com.jdm.legends.users.service.enums.CarFuelType.GASOLINE;
import static com.jdm.legends.users.service.enums.CarTransmissionType.MANUAL_TRANSMISSION;
import static java.time.LocalDateTime.now;

@Service
public class UtilsMock {

    public static String writeJsonAsString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong while parsing the object", e);
        }
    }

    public static Car buildCarRequest() {
        Car build = Car.builder()
                .carName("Toyota Supra")
                .carColor(BLACK)
                .carCompany(TOYOTA)
                .carFuelType(GASOLINE)
                .carTransmissionType(MANUAL_TRANSMISSION)
                .damaged(false)
                .initialPrice(333900)
                .km(436522)
                .quantityInStock(4)
                .startDateCarPostedOnline(LocalDateTime.of(2020, 10, 10, 12, 12, 12))
                .deadlineCarToSell(now().plusMonths(3))
                .build();

        TemporaryUser temporaryUser = TemporaryUser.builder()
                .fullName("John Cena")
                .userName("cannot_see_me98")
                .emailAddress("johnCeva12@yahoo.com")
                .role("Potential Client")
                .checkInformationStoredTemporarily(true)
                .build();

        HistoryBid historyBid = HistoryBid.builder()
                .bidValue(new BigDecimal("12354323412"))
                .car(build)
                .timeOfTheBid(now())
                .temporaryUsersList(new HashSet<>(Set.of(temporaryUser)))
                .build();

        build.setHistoryBidList(new ArrayList<>(List.of(historyBid)));
        return build;
    }
}
