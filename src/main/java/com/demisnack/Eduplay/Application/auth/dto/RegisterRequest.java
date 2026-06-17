package com.demisnack.Eduplay.Application.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(min = 5, max = 50)
    private String name;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_FORMAT")
    private String email;

    @NotBlank (message = "PASSWORD_REQUIRED")
    @Size(min = 8, message = "PASSWORD_SIZE")
    private String password;

    @NotBlank(message = "Role tidak boleh kosong")
    private String role;

    @Builder.Default
    private String country = "ID";

}
