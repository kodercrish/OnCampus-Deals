package com.example.search.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SearchRequest {
    private String keyword;       // optional
    private String category;      // optional
    private Double minPrice;      // optional
    private Double maxPrice;      // optional
}
