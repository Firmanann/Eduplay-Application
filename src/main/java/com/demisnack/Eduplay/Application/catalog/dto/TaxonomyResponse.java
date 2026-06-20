package com.demisnack.Eduplay.Application.catalog.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TaxonomyResponse {
    private List<String> categories;
    private List<String> subjects;
    private List<String> gradeLevels;
}
