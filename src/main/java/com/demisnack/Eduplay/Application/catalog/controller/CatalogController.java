package com.demisnack.Eduplay.Application.catalog.controller;

import com.demisnack.Eduplay.Application.catalog.dto.CatalogResponse;
import com.demisnack.Eduplay.Application.catalog.dto.PurchaseResponse;
import com.demisnack.Eduplay.Application.catalog.dto.TaxonomyResponse;
import com.demisnack.Eduplay.Application.catalog.service.CatalogService;
import com.demisnack.Eduplay.Application.global.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping
    public ResponseEntity<GlobalResponse<List<CatalogResponse>>> getCatalog(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String subject) {

        List<CatalogResponse> responseData = catalogService.getCatalog(category, subject);

        GlobalResponse<List<CatalogResponse>> response = GlobalResponse.<List<CatalogResponse>>builder()
                .success(true)
                .data(responseData)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<CatalogResponse>> getCatalogDetail(@PathVariable String id) {
        CatalogResponse data = catalogService.getCatalogDetail(id);

        GlobalResponse<CatalogResponse> response = GlobalResponse.<CatalogResponse>builder()
                .success(true)
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/taxonomies")
    public ResponseEntity<GlobalResponse<TaxonomyResponse>> getTaxonomies() {
        TaxonomyResponse data = catalogService.getTaxonomies();

        GlobalResponse<TaxonomyResponse> response = GlobalResponse.<TaxonomyResponse>builder()
                .success(true)
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<GlobalResponse<PurchaseResponse>> purchaseCatalog(@PathVariable String id, Principal principal) {
        PurchaseResponse data = catalogService.purchaseCatalog(id, principal.getName());

        GlobalResponse<PurchaseResponse> response = GlobalResponse.<PurchaseResponse>builder()
                .success(true)
                .data(data)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
