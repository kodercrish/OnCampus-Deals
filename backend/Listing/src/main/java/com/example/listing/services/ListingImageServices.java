package com.example.listing.services;

import com.example.listing.entities.ListingImages;
import com.example.listing.entities.Listings;
import com.example.listing.repositories.ListingImagesRepository;
import com.example.listing.repositories.ListingsRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingImageServices {

    private final ListingImagesRepository listingImagesRepository;
    private final ListingsRepository listingsRepository;

    /** Save one image */
    public String saveImage(ListingImages img) {
        listingImagesRepository.save(img);
        return img.getId();
    }

    /** Delete all images for a listing */
    public void deleteImagesByListingId(String listingId) {
        List<ListingImages> imgs = listingImagesRepository.findByListingId(listingId);
        listingImagesRepository.deleteAll(imgs);
    }

    /** Fetch images of a listing */
    public List<ListingImages> fetchListingImages(String listingId) {
        return listingImagesRepository.findByListingId(listingId);
    }

    /** Add additional images after creation */
    public String addImages(String listingId, List<String> urls) {
        Listings listing = listingsRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        for (String url : urls) {
            ListingImages img = new ListingImages();
            img.setListing(listing);
            img.setImageUrl(url);
            listingImagesRepository.save(img);
        }

        return "Images added successfully";
    }

    /** * NEW METHOD: Delete a single image by Image ID 
     * This fixes the error in ListingController
     */
    public String deleteImage(String imageId) {
        // 1. Check if image exists
        if (!listingImagesRepository.existsById(imageId)) {
            throw new RuntimeException("Image not found with ID: " + imageId);
        }

        // 2. Delete from Database
        listingImagesRepository.deleteById(imageId);

        return "Image deleted successfully";
    }
}