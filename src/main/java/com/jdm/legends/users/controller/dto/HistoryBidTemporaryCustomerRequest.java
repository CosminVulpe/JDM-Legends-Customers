package com.jdm.legends.users.controller.dto;


import com.jdm.legends.users.service.entity.HistoryBid;

public record HistoryBidTemporaryCustomerRequest(
        HistoryBid historyBid,
        TemporaryCustomerRequest temporaryCustomerRequest
) {
}
