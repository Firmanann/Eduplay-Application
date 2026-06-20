package com.demisnack.Eduplay.Application.library.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class LibraryResponse {
    private String purchaseId;
    private LocalDateTime purchasedAt;
    private ContentDto content;
}