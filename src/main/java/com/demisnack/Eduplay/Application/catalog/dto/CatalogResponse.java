package com.demisnack.Eduplay.Application.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CatalogResponse {

    private String id;
    private String title;
    private String description;
    private Integer price;
    private String fileUrl;
    private String thumbnailUrl;
    private String category;
    private String subject;
    private String gradeLevel;
    private String contributorName;
    private LocalDateTime createdAt;
}