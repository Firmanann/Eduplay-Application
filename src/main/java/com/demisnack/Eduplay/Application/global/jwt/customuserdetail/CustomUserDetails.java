package com.demisnack.Eduplay.Application.global.jwt.customuserdetail;

import com.demisnack.Eduplay.Application.user.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    private final UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    // Pake UUID sesuai rancangan database EduPlay kita
    public UUID getId() {
        return user.getId();
    }

    // Langsung kita tembak ke nama Role dari relasi database lu
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().getStatus()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Betul, kita pakai email
    }

    // --- 4 METHOD STATUS AKUN BAWAAN SPRING SECURITY (WAJIB ADA SEMUA) ---
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}