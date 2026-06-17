package com.demisnack.Eduplay.Application.user.service;

import com.demisnack.Eduplay.Application.auth.dto.RegisterRequest;
import com.demisnack.Eduplay.Application.global.exception.BusinessException;
import com.demisnack.Eduplay.Application.global.exception.ErrorCode;
import com.demisnack.Eduplay.Application.roles.entity.RolesEntity;
import com.demisnack.Eduplay.Application.roles.entity.RolesStatus;
import com.demisnack.Eduplay.Application.roles.repository.RolesRepository;
import com.demisnack.Eduplay.Application.security.service.PasswordService;
import com.demisnack.Eduplay.Application.user.entity.UserEntity;
import com.demisnack.Eduplay.Application.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    //Inject Class
    private final UserRepository userRepo;
    private final PasswordService passwordService;
    private final RolesRepository roleRepo;

    //To validate existing email
    public void validateEmailAvailability(String email) {
        if (userRepo.existsByEmail(email)) {
            throw new BusinessException(ErrorCode.EMAIL_EXISTS);
        }
    }

    @Transactional
    public UserEntity createUser(RegisterRequest request) {

        // 1. Validasi ketersediaan email
        validateEmailAvailability(request.getEmail());

        // 2. Cari role berdasarkan input string
        RolesEntity userRole = roleRepo.findByStatus(request.getRole())
                .orElseThrow(() -> new BusinessException(ErrorCode.ROLE_NOT_FOUND));

        // 3. Hash Password menggunakan passwordEncoder bawaan SecurityConfig
        String hashedPassword = passwordService.hashPassword(request.getPassword());

        // 4. Membuat objek baru dan set data sesuai field EduPlay
        UserEntity newUser = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())

                .password(hashedPassword)
                .role(userRole)
                .country(request.getCountry())
                .build();

        return userRepo.save(newUser);
    }


}
