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

        if (min == null) min = 0.0;
        if (max == null) max = 9999999.0;

        return repo.search(keyword, category, min, max);
    }
}
