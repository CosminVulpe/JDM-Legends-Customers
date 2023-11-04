package com.jdm.legends.users.service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class HistoryBidTemporaryUser {
    private HistoryBid historyBid;
    private TemporaryUser temporaryUser;
}
