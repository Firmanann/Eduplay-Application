package com.demisnack.Eduplay.Application.auth.controller;


import com.demisnack.Eduplay.Application.auth.dto.*;
import com.demisnack.Eduplay.Application.auth.service.AuthService;
import com.demisnack.Eduplay.Application.global.response.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController (AuthService authService){
        this.authService = authService;
    }

    //catch register endpoint
    @PostMapping("/register")
    public ResponseEntity<GlobalResponse<RegisterResponse>> register (@Valid @RequestBody RegisterRequest request) {

        // Eksekusi logic register di service
        RegisterResponse responseData = authService.register(request);

        // Desain format GlobalResponse standar
        GlobalResponse<RegisterResponse> response = GlobalResponse.<RegisterResponse>builder()
                .success(true)
                .data(responseData)
                .build();

        // Return status 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //catch login endpoint
    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        // Eksekusi logic login di service
        LoginResponse responseData = authService.login(request);

        // Bungkus output ke dalam format GlobalResponse standar
        GlobalResponse<LoginResponse> response = GlobalResponse.<LoginResponse>builder()
                .success(true)
                .data(responseData)
                .build();

        // Return dengan status 200 OK
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<GlobalResponse<UserProfileResponse>> getMe(Principal principal) {

        //Principal = email user
        UserProfileResponse responseData = authService.getMe(principal.getName());

        GlobalResponse<UserProfileResponse> response = GlobalResponse.<UserProfileResponse>builder()
                .success(true)
                .data(responseData)
                .build();

        return ResponseEntity.ok(response);
    }

}
