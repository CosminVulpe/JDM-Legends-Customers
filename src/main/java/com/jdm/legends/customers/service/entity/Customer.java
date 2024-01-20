package com.jdm.legends.customers.service.entity;

import com.jdm.legends.customers.service.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @Enumerated(EnumType.STRING)
    private Roles role;
    private String pwd;
}
