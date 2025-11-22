package com.example.admin.services;

import com.example.admin.entities.ListingAdminView;
import com.example.admin.repositories.ListingAdminRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ListingAdminRepository listingRepo;

    /** Fetch all listings */
    public List<ListingAdminView> fetchAllListings() {
        return listingRepo.findAll();
    }

    /** Delete a listing */
    public String deleteListing(String listingId) {
        ListingAdminView listing = listingRepo.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        listingRepo.delete(listing);
        return "Listing deleted successfully";
    }

    /** Mark listing as UNAVAILABLE */
    public String markUnavailable(String listingId) {
        ListingAdminView listing = listingRepo.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        listing.setStatus("UNAVAILABLE");
        listingRepo.save(listing);

        return "Listing marked as unavailable";
    }
}
