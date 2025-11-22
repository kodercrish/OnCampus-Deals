package com.example.search;

import com.example.search.entities.ListingSearchView;
import com.example.search.repositories.ListingSearchRepository;
import com.example.search.services.SearchService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    private ListingSearchRepository repo;
    private SearchService service;

    @BeforeEach
    void setup() {
        repo = mock(ListingSearchRepository.class);
        service = new SearchService(repo);
    }

    @Test
    void testSearchWithKeyword() {

        ListingSearchView item = new ListingSearchView();
        item.setTitle("iPhone 12");

        when(repo.search("iphone", null, 0.0, 9999999.0))
                .thenReturn(List.of(item));

        var results = service.search("iphone", null, null, null);

        assertEquals(1, results.size());
        assertEquals("iPhone 12", results.get(0).getTitle());
    }
}
