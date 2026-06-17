package com.demisnack.Eduplay.Application.auth.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private UUID id;
    private String name;
    private String email;
    private String role;
    @JsonProperty("created_at")
    private String createdAt;
}
