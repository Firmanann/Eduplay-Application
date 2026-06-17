package com.demisnack.Eduplay.Application.auth.service;

import com.demisnack.Eduplay.Application.auth.dto.*;
import com.demisnack.Eduplay.Application.global.exception.BusinessException;
import com.demisnack.Eduplay.Application.global.exception.ErrorCode;
import com.demisnack.Eduplay.Application.global.jwt.customuserdetail.CustomUserDetails;
import com.demisnack.Eduplay.Application.global.jwt.service.JwtService;
import com.demisnack.Eduplay.Application.user.entity.UserEntity;
import com.demisnack.Eduplay.Application.user.repository.UserRepository;
import com.demisnack.Eduplay.Application.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    public LoginResponse login(LoginRequest request) {

        try {
            // 1. Serahkan validasi email & password ke satpam Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            // Tangkap error kalau password salah atau email nggak terdaftar
            throw new BusinessException(ErrorCode.EMAIL_PASSWORD_INVALID);
        }

        // 2. Ambil data user dari database (Pasti ketemu karena lolos auth di atas)
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(ErrorCode.EMAIL_NOT_FOUND));

        // 3. Bungkus data user ke dalam CustomUserDetails
        CustomUserDetails customUser = new CustomUserDetails(user);

        // 4. Generate Access Token JWT (Tanpa Refresh Token)
        String accessToken = jwtService.generateToken(customUser);

        // 5. Susun dan kembalikan data Response-nya (Sesuai DTO LoginResponse yang baru)
        return LoginResponse.builder()
                .accessToken(accessToken)
                .id(user.getId())
                .name(user.getName()) // Pakai name, bukan username
                .email(user.getEmail())
                .role(user.getRole().getStatus()) // Ambil dari field status di RolesEntity
                .build();
    }

    public UserProfileResponse getMe(String email) {

        // 1. Cari user berdasarkan email yang diekstrak dari Token
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan")); // Ganti dengan BusinessException lu

        // 2. Logic penentuan currency sederhana
        String currency = "ID".equalsIgnoreCase(user.getCountry()) ? "IDR" : "USD";

        // 3. Bungkus jadi DTO
        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getStatus())
                .country(user.getCountry())
                .currency(currency)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
