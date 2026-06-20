package com.demisnack.Eduplay.Application.contributor.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MyContentResponse {
    private String id;
    private String title;
    private Integer price;
    private LocalDateTime createdAt;
}