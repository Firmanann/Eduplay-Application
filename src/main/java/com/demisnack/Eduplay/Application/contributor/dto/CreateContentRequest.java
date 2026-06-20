package com.demisnack.Eduplay.Application.contributor.dto;

import lombok.Data;

@Data
public class CreateContentRequest {

    private String title;
    private String description;
    private Integer price;
    private String fileUrl;
    private String thumbnailUrl;
    private String category;
    private String subject;
    private String gradeLevel;
}
