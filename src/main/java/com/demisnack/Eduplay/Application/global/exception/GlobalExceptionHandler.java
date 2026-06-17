package com.demisnack.Eduplay.Application.global.exception;

import com.demisnack.Eduplay.Application.global.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Input Exception Handler (Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {

        List<GlobalResponse.ErrorDetail> errorDetails = new ArrayList<>();

        // Loop to take all error fields dan konversi ke ErrorDetail
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorKey = error.getDefaultMessage();
            String finalMessage = errorKey;

            try {
                // Translate message from ErrorCode
                ErrorCode errorCode = ErrorCode.valueOf(errorKey);
                finalMessage = errorCode.getMessage();
            } catch (IllegalArgumentException e) {
                // Jaring pengaman
            }

            errorDetails.add(GlobalResponse.ErrorDetail.builder()
                    .field(fieldName)
                    .message(finalMessage)
                    .build());
        });

        // Bungkus pakai GlobalResponse sesuai API Docs
        GlobalResponse<Void> response = GlobalResponse.<Void>builder()
                .success(false)
                .error(GlobalResponse.ErrorInfo.builder()
                        .code("VALIDATION_ERROR")
                        .message("Satu atau lebih field tidak lolos validasi")
                        .details(errorDetails)
                        .build())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 2. Business Exception Handler
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<GlobalResponse<Void>> handleBusinessException(BusinessException ex) {

        HttpStatus status = ex.getErrorCode().getHttpStatus();

        // Bungkus pakai GlobalResponse
        GlobalResponse<Void> response = GlobalResponse.<Void>builder()
                .success(false)
                .error(GlobalResponse.ErrorInfo.builder()
                        .code(ex.getErrorCode().name()) // Ambil code dari enum, misal: NOT_FOUND
                        .message(ex.getMessage())
                        .build())
                .build();

        return ResponseEntity.status(status).body(response);
    }
}