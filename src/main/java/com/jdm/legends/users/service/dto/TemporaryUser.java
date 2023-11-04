package com.jdm.legends.users.service.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "TemporaryUser")
@Table
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class TemporaryUser {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Nullable
    private String fullName;

    @Nullable
    private String userName;

    @Email
    private String emailAddress;

    @Nullable
    private String role;

    private boolean checkInformationStoredTemporarily;

    @ManyToMany(fetch = EAGER, cascade = MERGE)
    @JoinTable(
            name = "temporary_user_history_bid",
            joinColumns =  @JoinColumn(name = "temporary_user_id") ,
            inverseJoinColumns =  @JoinColumn(name = "history_bid_id")
    )
    @JsonIgnore
    private List<HistoryBid> historyBidList = new ArrayList<>();

    public void addHistoryBid(HistoryBid historyBid) {
        historyBidList.add(historyBid);
        historyBid.getTemporaryUsersList().add(this);
    }
}
