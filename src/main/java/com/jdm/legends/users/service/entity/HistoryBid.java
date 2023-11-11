package com.jdm.legends.users.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jdm.legends.users.service.entity.Car;
import com.jdm.legends.users.service.entity.TemporaryCustomer;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class HistoryBid {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private BigDecimal bidValue;

    private LocalDateTime timeOfTheBid;

    @ManyToOne(cascade = MERGE, fetch = LAZY)
    @JoinColumn(name = "car_id", referencedColumnName = "id")
    @JsonIgnore
    private Car car;

    @ManyToMany(mappedBy = "historyBidList")
    private Set<TemporaryCustomer> temporaryUsersList = new HashSet<>();

}
