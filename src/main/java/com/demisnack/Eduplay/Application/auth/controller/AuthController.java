package com.demisnack.Eduplay.Application.auth.controller;


import com.demisnack.Eduplay.Application.auth.dto.RegisterRequest;
import com.demisnack.Eduplay.Application.auth.dto.RegisterResponse;
import com.demisnack.Eduplay.Application.auth.service.AuthService;
import com.demisnack.Eduplay.Application.global.response.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
