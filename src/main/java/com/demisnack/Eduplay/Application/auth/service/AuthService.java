package com.demisnack.Eduplay.Application.auth.service;

import com.demisnack.Eduplay.Application.auth.dto.RegisterRequest;
import com.demisnack.Eduplay.Application.auth.dto.RegisterResponse;
import com.demisnack.Eduplay.Application.global.jwt.service.JwtService;
import com.demisnack.Eduplay.Application.user.entity.UserEntity;
import com.demisnack.Eduplay.Application.user.repository.UserRepository;
import com.demisnack.Eduplay.Application.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public RegisterResponse register(RegisterRequest request){

        //Call userservice and put to the new object
        UserEntity savedUser = userService.createUser(request);

        //Mapping userEntity to RegisterResponse (Designing Success Output)
        return RegisterResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().getStatus())
                .createdAt(savedUser.getCreatedAt().toString())
                .build();
    }
}
