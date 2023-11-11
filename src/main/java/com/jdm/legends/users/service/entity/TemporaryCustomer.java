package com.jdm.legends.users.service.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "temporary_customers")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class TemporaryCustomer {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String fullName;
    private String userName;
    private String emailAddress;
    private String role;
    private boolean checkInformationStoredTemporarily;

    @ElementCollection
    @CollectionTable(name = "temporary_customer_history_bids", joinColumns = @JoinColumn(name = "temporary_customer_id"))
    @Column(name = "history_bid")
    private List<Long> historyBidIds;
}
