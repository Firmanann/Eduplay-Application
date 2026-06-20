package com.demisnack.Eduplay.Application.contributor.controller;

import com.demisnack.Eduplay.Application.contributor.dto.*;
import com.demisnack.Eduplay.Application.contributor.service.ContributorService;
import com.demisnack.Eduplay.Application.global.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContributorService contributorService;

    @PostMapping
    public ResponseEntity<GlobalResponse<Void>> createContent(
            @RequestBody CreateContentRequest request, Principal principal) {

        contributorService.createContent(request, principal.getName());

        GlobalResponse<Void> response = GlobalResponse.<Void>builder()
                .success(true)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Tambahin import List dan MyContentResponse
    @GetMapping("/my-contents")
    public ResponseEntity<GlobalResponse<List<MyContentResponse>>> getMyContents(Principal principal) {

        List<MyContentResponse> data = contributorService.getMyContents(principal.getName());

        GlobalResponse<List<MyContentResponse>> response = GlobalResponse.<List<MyContentResponse>>builder()
                .success(true)
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> updateContent(@PathVariable String id, @RequestBody UpdateContentRequest request, Principal principal) {

        contributorService.updateContent(id, request, principal.getName());

        GlobalResponse<Void> response = GlobalResponse.<Void>builder()
                .success(true)
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteContent(@PathVariable String id, Principal principal) {

        contributorService.deleteContent(id, principal.getName());

        GlobalResponse<Void> response = GlobalResponse.<Void>builder()
                .success(true)
                .data(null)
                .build();

        return ResponseEntity.ok(response);
    }

}