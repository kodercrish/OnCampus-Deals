package com.example.listing.repositories;

import com.example.listing.entities.ListingImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingImagesRepository extends JpaRepository<ListingImages, String> {

    List<ListingImages> findByListingId(String listingId);
}
