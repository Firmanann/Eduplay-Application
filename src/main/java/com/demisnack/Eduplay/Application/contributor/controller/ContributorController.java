package com.demisnack.Eduplay.Application.contributor.controller;

import com.demisnack.Eduplay.Application.contributor.dto.BalanceResponse;
import com.demisnack.Eduplay.Application.contributor.dto.TransactionResponse;
import com.demisnack.Eduplay.Application.contributor.service.ContributorService;
import com.demisnack.Eduplay.Application.global.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/contributor")
@RequiredArgsConstructor
public class ContributorController {

    private final ContributorService contributorService;

    @GetMapping("/balance")
    public ResponseEntity<GlobalResponse<BalanceResponse>> getBalance(Principal principal) {

        BalanceResponse data = contributorService.getBalance(principal.getName());

        return ResponseEntity.ok(GlobalResponse.<BalanceResponse>builder()
                .success(true)
                .data(data)
                .build());
    }

    @GetMapping("/transactions")
    public ResponseEntity<GlobalResponse<List<TransactionResponse>>> getTransactions(Principal principal) {

        List<TransactionResponse> data = contributorService.getTransactions(principal.getName());

        return ResponseEntity.ok(GlobalResponse.<List<TransactionResponse>>builder()
                .success(true)
                .data(data)
                .build());
    }
}