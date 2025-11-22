package com.example.search.repositories;

import com.example.search.entities.ListingSearchView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ListingSearchRepository extends JpaRepository<ListingSearchView, String> {

    @Query("""
        SELECT l FROM ListingSearchView l
        WHERE (:keyword IS NULL OR LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:category IS NULL OR LOWER(l.category) = LOWER(:category))
        AND (:min IS NULL OR l.price >= :min)
        AND (:max IS NULL OR l.price <= :max)
        """)
    List<ListingSearchView> search(
            @Param("keyword") String keyword,
            @Param("category") String category,
            @Param("min") Double min,
            @Param("max") Double max
    );
}
