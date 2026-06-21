package com.demisnack.Eduplay.Application.contributor.dto;

import lombok.Data;

@Data
public class UpdateContentRequest {
    private String title;
    private String description;
    private Integer price;
    private String category;
    private String subject;
    private String gradeLevel;
    private String fileUrl;
    private String thumbnailUrl;

}