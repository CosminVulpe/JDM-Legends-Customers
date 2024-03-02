package com.jdm.legends.customers.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String userName;
    private String emailAddress;
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roles_id", referencedColumnName = "id")
    private Role role;
    private String pwd;

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
