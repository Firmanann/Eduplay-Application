package com.demisnack.Eduplay.Application.catalog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PurchaseResponse {

    private String purchaseId;
    private String contentId;
    private Integer pricePaid;
}