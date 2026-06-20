package com.demisnack.Eduplay.Application.library.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayResponse {
    private String fileUrl;
}