package com.example.listing.repositories;

import com.example.listing.entities.Listings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingsRepository extends JpaRepository<Listings, String> {

    List<Listings> findBySellerId(String sellerId);

    List<Listings> findByStatus(Listings.Status status);

    List<Listings> findByCategoryIgnoreCase(String category);

    List<Listings> findByTitleContainingIgnoreCase(String keyword);
}
