package com.example.search.controllers;

import com.example.search.constants.ApiEndpoints;
import com.example.search.dto.SearchRequest;
import com.example.search.dto.SearchResponse;
import com.example.search.services.SearchService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoints.SEARCH_BASE)
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping(ApiEndpoints.SEARCH_LISTINGS)
    public ResponseEntity<SearchResponse> search(@RequestBody SearchRequest req) {
        try {
            var results = searchService.search(
                    req.getKeyword(),
                    req.getCategory(),
                    req.getMinPrice(),
                    req.getMaxPrice()
            );

            return ResponseEntity.ok(new SearchResponse(results));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new SearchResponse(null));
        }
    }
}
