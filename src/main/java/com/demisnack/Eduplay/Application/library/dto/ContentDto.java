package com.demisnack.Eduplay.Application.library.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContentDto {
    private String id;
    private String title;
    private String thumbnailUrl;
}