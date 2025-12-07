package com.example.search.services;

import com.example.search.entities.ListingSearchView;
import com.example.search.repositories.ListingSearchRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ListingSearchRepository repo;

    public List<ListingSearchView> search(String keyword, String category, Double min, Double max) {
        if (min == null || min < 0) min = 0.0; // Default to 0.0 if min is null or negative
        if (max == null) max = 9999999.0; // Default to a very high value if max is null

        return repo.search(keyword, category, min, max)
                   .stream()
                   // safer, null-check and case-insensitive match
                   .filter(listing -> listing.getStatus() != null && "ACTIVE".equalsIgnoreCase(listing.getStatus()))
                   .toList();
    }
}
