package com.example.search.dto;

import lombok.*;
import com.example.search.entities.ListingSearchView;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SearchResponse {
    private List<ListingSearchView> results;
}
