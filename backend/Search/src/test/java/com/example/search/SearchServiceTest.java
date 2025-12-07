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
        ListingSearchView item1 = new ListingSearchView();
        item1.setTitle("iPhone 12");
        item1.setStatus("ACTIVE");

        ListingSearchView item2 = new ListingSearchView();
        item2.setTitle("iPhone 13");
        item2.setStatus("ACTIVE");

        ListingSearchView item3 = new ListingSearchView();
        item3.setTitle("iPhone Case");
        item3.setStatus("ACTIVE");

        when(repo.search("iphone", null, 0.0, 9999999.0))
                .thenReturn(List.of(item1, item2, item3));

        var results = service.search("iphone", null, null, null);

        assertEquals(3, results.size());
        assertEquals("iPhone 12", results.get(0).getTitle());
        assertEquals("iPhone 13", results.get(1).getTitle());
        assertEquals("iPhone Case", results.get(2).getTitle());
    }

    @Test
    void testSearchWithCategory() {
        ListingSearchView item1 = new ListingSearchView();
        item1.setTitle("iPhone 12");
        item1.setStatus("ACTIVE");

        ListingSearchView item2 = new ListingSearchView();
        item2.setTitle("Laptop HP");
        item2.setStatus("ACTIVE");

        ListingSearchView item3 = new ListingSearchView();
        item3.setTitle("Samsung TV");
        item3.setStatus("ACTIVE");

        ListingSearchView item4 = new ListingSearchView();
        item4.setTitle("iPhone 13");
        item4.setStatus("ACTIVE");

        when(repo.search(null, "electronics", 0.0, 9999999.0))
                .thenReturn(List.of(item1, item2, item3, item4));

        var results = service.search(null, "electronics", null, null);

        assertEquals(4, results.size());
        assertEquals("iPhone 12", results.get(0).getTitle());
        assertEquals("Laptop HP", results.get(1).getTitle());
        assertEquals("Samsung TV", results.get(2).getTitle());
        assertEquals("iPhone 13", results.get(3).getTitle());
    }

    @Test
    void testSearchWithPriceRange() {
        ListingSearchView item1 = new ListingSearchView();
        item1.setTitle("Wooden Table");
        item1.setStatus("ACTIVE");

        ListingSearchView item2 = new ListingSearchView();
        item2.setTitle("Pencil");
        item2.setStatus("ACTIVE");

        ListingSearchView item3 = new ListingSearchView();
        item3.setTitle("Unknown Item");
        item3.setStatus("ACTIVE");

        when(repo.search(null, null, 0.0, 10000.0))
                .thenReturn(List.of(item1, item2, item3));

        var results = service.search(null, null, 0.0, 10000.0);

        assertEquals(3, results.size());
        assertEquals("Wooden Table", results.get(0).getTitle());
        assertEquals("Pencil", results.get(1).getTitle());
        assertEquals("Unknown Item", results.get(2).getTitle());
    }

    @Test
    void testSearchWithNullKeywordAndCategory() {
        ListingSearchView item = new ListingSearchView();
        item.setTitle("Generic Item");
        item.setStatus("ACTIVE");

        when(repo.search(null, null, 0.0, 9999999.0))
                .thenReturn(List.of(item));

        var results = service.search(null, null, null, null);

        assertEquals(1, results.size());
        assertEquals("Generic Item", results.get(0).getTitle());
    }

    @Test
    void testSearchWithNullMaxPrice() {
        ListingSearchView item = new ListingSearchView();
        item.setTitle("Expensive Item");
        item.setStatus("ACTIVE");

        when(repo.search(null, null, 0.0, 9999999.0))
                .thenReturn(List.of(item));

        var results = service.search(null, null, null, null);

        verify(repo).search(null, null, 0.0, 9999999.0); // Max price should default to 9999999.0
        assertEquals(1, results.size());
        assertEquals("Expensive Item", results.get(0).getTitle());
    }
}
