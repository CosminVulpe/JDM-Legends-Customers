package com.jdm.legends.users.service.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jdm.legends.users.service.dto.HistoryBid;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
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

    @ManyToMany(fetch = EAGER, cascade = MERGE)
    @JoinTable(
            name = "temporary_customer_history_bid",
            joinColumns =  @JoinColumn(name = "temporary_customer_id") ,
            inverseJoinColumns =  @JoinColumn(name = "history_bid_id")
    )
    @JsonIgnore
    private List<HistoryBid> historyBidList = new ArrayList<>();

    public void addHistoryBid(HistoryBid historyBid) {
        historyBidList.add(historyBid);
        historyBid.getTemporaryUsersList().add(this);
    }
}
