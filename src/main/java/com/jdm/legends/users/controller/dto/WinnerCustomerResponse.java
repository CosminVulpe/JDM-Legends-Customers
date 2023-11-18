package com.jdm.legends.users.controller.dto;

import java.math.BigDecimal;

public record WinnerCustomerResponse(
        BigDecimal bidValue
        , Long historyBidId
        , String userName
        , String emailAddress
) {
}
