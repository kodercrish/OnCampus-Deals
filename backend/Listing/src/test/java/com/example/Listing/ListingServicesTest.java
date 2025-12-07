package com.example.listing.services;

import com.example.listing.dto.create.CreateListingRequest;
import com.example.listing.entities.ListingImages;
import com.example.listing.entities.Listings;
import com.example.listing.repositories.ListingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListingServicesTest {

    private ListingsRepository listingsRepository;
    private ListingImageServices listingImageServices;
    private CloudinaryService cloudinaryService;
    private ListingServices service;

    @BeforeEach
    void setup() {
        listingsRepository = mock(ListingsRepository.class);
        listingImageServices = mock(ListingImageServices.class);
        cloudinaryService = mock(CloudinaryService.class);
        service = new ListingServices(listingsRepository, listingImageServices, cloudinaryService);
    }

    @Test
    void createListingWithImages_shouldCreateListingAndUploadImages_whenImagesPresent() {
        // Mock DTO by using a Mockito mock (avoids constructor assumptions)
        CreateListingRequest req = mock(CreateListingRequest.class);
        when(req.getTitle()).thenReturn("Title");
        when(req.getDescription()).thenReturn("Desc");
        when(req.getPrice()).thenReturn(123.0);
        when(req.getCategory()).thenReturn("cat");
        when(req.getSellerId()).thenReturn("seller1");

        MultipartFile mf1 = mock(MultipartFile.class);
        MultipartFile mf2 = mock(MultipartFile.class);
        when(mf1.isEmpty()).thenReturn(false);
        when(mf2.isEmpty()).thenReturn(false);

        when(cloudinaryService.uploadImage(mf1)).thenReturn("http://c/a.jpg");
        when(cloudinaryService.uploadImage(mf2)).thenReturn("http://c/b.jpg");

        // Save should return the same listing; repository.save will be called, but we don't need to stub it
        String listingId = service.createListingWithImages(req, List.of(mf1, mf2));

        assertNotNull(listingId);
        // ensure cloudinary and saveImage are used
        verify(cloudinaryService).uploadImage(mf1);
        verify(cloudinaryService).uploadImage(mf2);
        // listingImageServices.saveImage should be called twice (for two urls)
        verify(listingImageServices, times(2)).saveImage(any(ListingImages.class));
        verify(listingsRepository).save(any(Listings.class));
    }

    @Test
    void createListingWithImages_shouldCreateListing_whenImagesNullOrEmpty() {
        CreateListingRequest req = mock(CreateListingRequest.class);
        when(req.getTitle()).thenReturn("Title2");
        when(req.getDescription()).thenReturn("Desc2");
        when(req.getPrice()).thenReturn(999.0);
        when(req.getCategory()).thenReturn("cat2");
        when(req.getSellerId()).thenReturn("seller2");

        String listingId = service.createListingWithImages(req, null);

        assertNotNull(listingId);
        verify(cloudinaryService, never()).uploadImage(any());
        verify(listingImageServices, never()).saveImage(any());
        verify(listingsRepository).save(any(Listings.class));
    }

    @Test
    void editListing_shouldUpdateFields_whenListingExists() {
        String id = "L1";
        Listings listing = new Listings();
        listing.setId(id);
        listing.setTitle("old");
        listing.setDescription("olddesc");
        listing.setPrice(10.0);
        listing.setCategory("oldcat");

        when(listingsRepository.findById(id)).thenReturn(Optional.of(listing));

        String msg = service.editListing(id, "newTitle", "newDesc", 20.0, "newCat");

        assertEquals("Listing updated successfully", msg);
        verify(listingsRepository).findById(id);
        verify(listingsRepository).save(listing);
        assertEquals("newTitle", listing.getTitle());
        assertEquals(20.0, listing.getPrice());
        assertEquals("newCat", listing.getCategory());
    }

    @Test
    void editListing_shouldThrow_whenNotFound() {
        String id = "LNO";
        when(listingsRepository.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(RuntimeException.class, () -> service.editListing(id, "t", "d", 1.0, "c"));
        assertTrue(ex.getMessage().contains("Listing not found"));
        verify(listingsRepository).findById(id);
        verify(listingsRepository, never()).save(any());
    }

    @Test
    void deleteListing_shouldDeleteListingAndImages_whenExists() {
        String id = "L1";
        Listings listing = new Listings();
        listing.setId(id);

        when(listingsRepository.findById(id)).thenReturn(Optional.of(listing));

        String msg = service.deleteListing(id);

        assertEquals("Listing deleted successfully", msg);
        verify(listingsRepository).findById(id);
        verify(listingImageServices).deleteImagesByListingId(id);
        verify(listingsRepository).delete(listing);
    }

    @Test
    void deleteListing_shouldThrow_whenNotFound() {
        String id = "LNO";
        when(listingsRepository.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(RuntimeException.class, () -> service.deleteListing(id));
        assertTrue(ex.getMessage().contains("Listing not found"));
        verify(listingsRepository).findById(id);
        verify(listingImageServices, never()).deleteImagesByListingId(any());
    }

    @Test
    void fetchListing_shouldReturnListing_whenExists() {
        String id = "L1";
        Listings listing = new Listings();
        listing.setId(id);

        when(listingsRepository.findById(id)).thenReturn(Optional.of(listing));

        var fetched = service.fetchListing(id);

        assertEquals(listing, fetched);
        verify(listingsRepository).findById(id);
    }

    @Test
    void fetchListing_shouldThrow_whenNotFound() {
        String id = "LNO";
        when(listingsRepository.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(RuntimeException.class, () -> service.fetchListing(id));
        assertTrue(ex.getMessage().contains("Listing not found"));
        verify(listingsRepository).findById(id);
    }

    @Test
    void fetchListingsBySeller_shouldReturnList() {
        Listings a = new Listings();
        a.setSellerId("S1");
        when(listingsRepository.findBySellerId("S1")).thenReturn(List.of(a));

        var res = service.fetchListingsBySeller("S1");

        assertEquals(1, res.size());
        assertEquals("S1", res.get(0).getSellerId());
        verify(listingsRepository).findBySellerId("S1");
    }

    @Test
    void fetchActiveListings_shouldReturnActive() {
        Listings a = new Listings();
        a.setStatus(Listings.Status.ACTIVE);
        when(listingsRepository.findByStatus(Listings.Status.ACTIVE)).thenReturn(List.of(a));

        var res = service.fetchActiveListings();

        assertEquals(1, res.size());
        assertEquals(Listings.Status.ACTIVE, res.get(0).getStatus());
        verify(listingsRepository).findByStatus(Listings.Status.ACTIVE);
    }

    @Test
    void markAsSold_shouldUpdateStatus() {
        String id = "L1";
        Listings listing = new Listings();
        listing.setId(id);
        listing.setStatus(Listings.Status.ACTIVE);

        when(listingsRepository.findById(id)).thenReturn(Optional.of(listing));

        String msg = service.markAsSold(id);

        assertEquals("Listing marked as SOLD", msg);
        assertEquals(Listings.Status.SOLD, listing.getStatus());
        verify(listingsRepository).save(listing);
    }

    @Test
    void markAsUnavailable_shouldUpdateStatus() {
        String id = "L2";
        Listings listing = new Listings();
        listing.setId(id);
        listing.setStatus(Listings.Status.ACTIVE);

        when(listingsRepository.findById(id)).thenReturn(Optional.of(listing));

        String msg = service.markAsUnavailable(id);

        assertEquals("Listing marked as UNAVAILABLE", msg);
        assertEquals(Listings.Status.UNAVAILABLE, listing.getStatus());
        verify(listingsRepository).save(listing);
    }

    @Test
    void markAsSold_shouldThrow_whenNotFound() {
        String id = "NO";
        when(listingsRepository.findById(id)).thenReturn(Optional.empty());

        var ex = assertThrows(RuntimeException.class, () -> service.markAsSold(id));
        assertTrue(ex.getMessage().contains("Listing not found"));
    }
}
