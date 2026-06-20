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
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Roles tidak ditemukan"),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "Email tidak terdaftar"),
    EMAIL_PASSWORD_INVALID(HttpStatus.UNAUTHORIZED, "Email atau Password salah"), // 401 Unauthorized untuk gagal login

    // Error untuk Catalog & Library
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "Game tidak ditemukan"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User tidak ditemukan"),
    ALREADY_OWNED(HttpStatus.BAD_REQUEST, "Lu udah punya game ini bro!"),
    INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "Saldo lu kurang bro!"),
    NOT_OWNED(HttpStatus.FORBIDDEN, "Lu belum beli game ini bro!");


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
