package com.demisnack.Eduplay.Application.contributor.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BalanceResponse {
    private Integer balance;
    private String bankName;
    private String bankAccount;
}