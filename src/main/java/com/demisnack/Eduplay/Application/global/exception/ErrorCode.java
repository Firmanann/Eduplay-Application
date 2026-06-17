package com.demisnack.Eduplay.Application.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // Input/Output error message (Kesalahan validasi dari sisi Client)
    USERNAME_REQUIRED(HttpStatus.BAD_REQUEST, "Nama wajib diisi"),
    EMAIL_REQUIRED(HttpStatus.BAD_REQUEST, "Email wajib diisi"),
    PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "Password wajib diisi"),
    EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "Email format not valid"),
    PASSWORD_SIZE(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters"),
    EMAIL_EXISTS(HttpStatus.CONFLICT, "Email sudah digunakan"), // 409 Conflict standar untuk duplikasi data
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Roles tidak ditemukan");




    private final HttpStatus httpStatus;
    private final String message;

    // Constructor
    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    // Getter untuk HTTP Status
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    // Getter untuk Message
    public String getMessage() {
        return message;
    }

}
