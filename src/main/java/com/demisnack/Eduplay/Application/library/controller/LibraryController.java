package com.demisnack.Eduplay.Application.library.controller;

import com.demisnack.Eduplay.Application.global.response.GlobalResponse;
import com.demisnack.Eduplay.Application.library.dto.DownloadResponse;
import com.demisnack.Eduplay.Application.library.dto.LibraryResponse;
import com.demisnack.Eduplay.Application.library.dto.PlayResponse;
import com.demisnack.Eduplay.Application.library.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/library")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;

    @GetMapping
    public ResponseEntity<GlobalResponse<List<LibraryResponse>>> getMyLibrary(Principal principal) {
        // Tarik data library berdasarkan email user dari token JWT
        List<LibraryResponse> data = libraryService.getMyLibrary(principal.getName());

        GlobalResponse<List<LibraryResponse>> response = GlobalResponse.<List<LibraryResponse>>builder()
                .success(true)
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/play")
    public ResponseEntity<GlobalResponse<PlayResponse>> playContent(@PathVariable String id, Principal principal) {

        PlayResponse data = libraryService.playGame(id, principal.getName());

        GlobalResponse<PlayResponse> response = GlobalResponse.<PlayResponse>builder()
                .success(true)
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<GlobalResponse<DownloadResponse>> downloadContent(@PathVariable String id, Principal principal) {

        DownloadResponse data = libraryService.downloadGame(id, principal.getName());

        GlobalResponse<DownloadResponse> response = GlobalResponse.<DownloadResponse>builder()
                .success(true)
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }
}