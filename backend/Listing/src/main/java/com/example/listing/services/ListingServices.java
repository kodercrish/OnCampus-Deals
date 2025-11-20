package com.example.listing.services;

import com.example.listing.dto.create.CreateListingRequest;
import com.example.listing.entities.Listings;
import com.example.listing.entities.ListingImages;
import com.example.listing.repositories.ListingsRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingServices {

    private final ListingsRepository listingsRepository;
    private final ListingImageServices listingImageServices;
    private final CloudinaryService cloudinaryService;

    /** Create listing + upload images */
    public String createListingWithImages(CreateListingRequest req, List<MultipartFile> images) {

        Listings listing = new Listings();
        listing.setTitle(req.getTitle());
        listing.setDescription(req.getDescription());
        listing.setPrice(req.getPrice());
        listing.setCategory(req.getCategory());
        listing.setSellerId(req.getSellerId());

        listingsRepository.save(listing);

        // Upload images
        List<String> urls = new ArrayList<>();
        for (MultipartFile img : images) {
            urls.add(cloudinaryService.uploadImage(img));
        }

        // Save URLs in DB
        for (String url : urls) {
            ListingImages li = new ListingImages();
            li.setListing(listing);
            li.setImageUrl(url);
            listingImageServices.saveImage(li);
        }

        return listing.getId();
    }

    /** Edit listing */
    public String editListing(String id, String title, String desc, Double price, String category) {
        Listings listing = listingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        if (title != null && !title.isEmpty()) listing.setTitle(title);
        if (desc != null && !desc.isEmpty()) listing.setDescription(desc);
        if (price != null) listing.setPrice(price);
        if (category != null && !category.isEmpty()) listing.setCategory(category);

        listing.setUpdatedAt(LocalDateTime.now());
        listingsRepository.save(listing);

        return "Listing updated successfully";
    }

    /** Delete listing */
    public String deleteListing(String id) {
        Listings listing = listingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        listingImageServices.deleteImagesByListingId(id);

        listingsRepository.delete(listing);
        return "Listing deleted successfully";
    }

    /** Fetch one listing */
    public Listings fetchListing(String id) {
        return listingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
    }

    /** Fetch all listings by seller */
    public List<Listings> fetchListingsBySeller(String sellerId) {
        return listingsRepository.findBySellerId(sellerId);
    }

    /** Fetch active listings */
    public List<Listings> fetchActiveListings() {
        return listingsRepository.findByStatus(Listings.Status.ACTIVE);
    }

    /** Mark sold */
    public String markAsSold(String id) {
        Listings listing = listingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        listing.setStatus(Listings.Status.SOLD);
        listing.setUpdatedAt(LocalDateTime.now());

        listingsRepository.save(listing);
        return "Listing marked as SOLD";
    }

    /** Mark unavailable */
    public String markAsUnavailable(String id) {
        Listings listing = listingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        listing.setStatus(Listings.Status.UNAVAILABLE);
        listing.setUpdatedAt(LocalDateTime.now());

        listingsRepository.save(listing);
        return "Listing marked as UNAVAILABLE";
    }
}
