package com.demisnack.Eduplay.Application.contributor.dto;

import lombok.Data;

@Data
public class UpdateContentRequest {
    private String title;
    private String description;
    private Integer price;
}