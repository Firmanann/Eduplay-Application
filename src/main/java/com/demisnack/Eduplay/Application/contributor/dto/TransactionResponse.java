package com.demisnack.Eduplay.Application.contributor.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {

    private String purchaseId;
    private String contentTitle;
    private String buyerName;
    private Integer pricePaid;
    private LocalDateTime purchasedAt;
}