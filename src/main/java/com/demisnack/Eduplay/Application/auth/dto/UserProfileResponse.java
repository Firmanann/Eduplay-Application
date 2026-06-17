package com.demisnack.Eduplay.Application.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {

    private UUID id;
    private String name;
    private String email;
    private String role;
    private String country;
    private String currency;
    private OffsetDateTime createdAt;

}