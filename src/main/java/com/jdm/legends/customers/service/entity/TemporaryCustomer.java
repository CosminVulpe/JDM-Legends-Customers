package com.jdm.legends.customers.service.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Arrays;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "temporary_customers")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemporaryCustomer {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String fullName;
    private String userName;
    private String emailAddress;
    private String role;
    private boolean checkInformationStoredTemporarily;
    private String historyBidId;
    private Long orderId;

    @JsonIgnore
    public boolean doesHistoryBidExists(String historyBidIdRequest) {
        boolean contains = historyBidId.contains(", ");
        if (contains) {
            List<String> ids = Arrays.stream(historyBidId.split(", ")).toList();
            return ids.contains(historyBidIdRequest);
        }
        return historyBidId.equalsIgnoreCase(historyBidIdRequest);
    }

}
