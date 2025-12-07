package com.example.listing.services;

import com.example.listing.entities.ListingImages;
import com.example.listing.entities.Listings;
import com.example.listing.repositories.ListingImagesRepository;
import com.example.listing.repositories.ListingsRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingImageServices {

    private final ListingImagesRepository listingImagesRepository;
    private final ListingsRepository listingsRepository;
    private final CloudinaryService cloudinaryService; // need Cloudinary service to upload MultipartFile

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

    /** Add additional images after creation (URLs) */
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

    /** Add images via MultipartFile list (uploads to cloudinary and persists records) */
    public String addImagesMultipart(String listingId, List<MultipartFile> images) {
        Listings listing = listingsRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        for (MultipartFile img : images) {
            if (img == null || img.isEmpty()) continue;
            String url = cloudinaryService.uploadImage(img);
            ListingImages li = new ListingImages();
            li.setListing(listing);
            li.setImageUrl(url);
            listingImagesRepository.save(li);
        }

        return "Images uploaded successfully";
    }

    /** Delete single image by ID */
    public String deleteImage(String imageId) {
        if (!listingImagesRepository.existsById(imageId)) {
            throw new RuntimeException("Image not found with ID: " + imageId);
        }
        listingImagesRepository.deleteById(imageId);
        return "Image deleted successfully";
    }
}
