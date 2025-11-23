package com.example.admin.services;

import com.example.admin.entities.ListingAdminView;
import com.example.admin.repositories.ListingAdminRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    private ListingAdminRepository listingRepo;
    private AdminService adminService;

    @BeforeEach
    void setup() {
        listingRepo = mock(ListingAdminRepository.class);
        adminService = new AdminService(listingRepo);
    }

    @Test
    void testFetchAllListings() {
        ListingAdminView item = new ListingAdminView();
        item.setId("L1");
        item.setTitle("Monitor");

        when(listingRepo.findAll()).thenReturn(List.of(item));

        var results = adminService.fetchAllListings();

        assertEquals(1, results.size());
        assertEquals("Monitor", results.get(0).getTitle());
    }

    @Test
    void testDeleteListing() {
        ListingAdminView listing = new ListingAdminView();
        listing.setId("L1");

        when(listingRepo.findById("L1")).thenReturn(Optional.of(listing));

        String msg = adminService.deleteListing("L1");

        verify(listingRepo, times(1)).delete(listing);
        assertEquals("Listing deleted successfully", msg);
    }

    @Test
    void testMarkUnavailable() {
        ListingAdminView listing = new ListingAdminView();
        listing.setId("L1");
        listing.setStatus("ACTIVE");

        when(listingRepo.findById("L1")).thenReturn(Optional.of(listing));

        String msg = adminService.markUnavailable("L1");

        assertEquals("UNAVAILABLE", listing.getStatus());
        verify(listingRepo, times(1)).save(listing);
        assertEquals("Listing marked as unavailable", msg);
    }
}
