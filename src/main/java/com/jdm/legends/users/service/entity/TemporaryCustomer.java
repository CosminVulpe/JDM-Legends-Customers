package com.jdm.legends.users.service.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private Long historyBidId;
}
