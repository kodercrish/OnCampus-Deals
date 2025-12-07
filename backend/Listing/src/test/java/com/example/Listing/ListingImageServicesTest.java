package com.example.listing.services;

import com.example.listing.entities.ListingImages;
import com.example.listing.entities.Listings;
import com.example.listing.repositories.ListingImagesRepository;
import com.example.listing.repositories.ListingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListingImageServicesTest {

    private ListingImagesRepository listingImagesRepository;
    private ListingsRepository listingsRepository;
    private CloudinaryService cloudinaryService;
    private ListingImageServices service;

    @BeforeEach
    void setup() {
        listingImagesRepository = mock(ListingImagesRepository.class);
        listingsRepository = mock(ListingsRepository.class);
        cloudinaryService = mock(CloudinaryService.class);
        service = new ListingImageServices(listingImagesRepository, listingsRepository, cloudinaryService);
    }

    @Test
    void saveImage_shouldSaveAndReturnId() {
        ListingImages img = new ListingImages();
        img.setImageUrl("http://example.com/1.jpg");
        // id is generated in entity by default
        when(listingImagesRepository.save(img)).thenReturn(img);

        String returnedId = service.saveImage(img);

        assertNotNull(returnedId);
        assertEquals(img.getId(), returnedId);
        verify(listingImagesRepository).save(img);
    }

    @Test
    void deleteImagesByListingId_shouldDeleteAllImages() {
        String listingId = "L1";
        ListingImages i1 = new ListingImages();
        ListingImages i2 = new ListingImages();
        List<ListingImages> imgs = List.of(i1, i2);

        when(listingImagesRepository.findByListingId(listingId)).thenReturn(imgs);

        service.deleteImagesByListingId(listingId);

        verify(listingImagesRepository).findByListingId(listingId);
        verify(listingImagesRepository).deleteAll(imgs);
    }

    @Test
    void fetchListingImages_shouldReturnImagesForListing() {
        String listingId = "L1";
        ListingImages i1 = new ListingImages();
        i1.setImageUrl("u1");
        List<ListingImages> imgs = List.of(i1);

        when(listingImagesRepository.findByListingId(listingId)).thenReturn(imgs);

        var res = service.fetchListingImages(listingId);

        assertEquals(1, res.size());
        assertEquals("u1", res.get(0).getImageUrl());
        verify(listingImagesRepository).findByListingId(listingId);
    }

    @Test
    void addImages_shouldAddUrls_whenListingExists() {
        String listingId = "L1";
        Listings listing = new Listings();
        listing.setId(listingId);

        when(listingsRepository.findById(listingId)).thenReturn(Optional.of(listing));

        List<String> urls = List.of("http://a.jpg", "http://b.jpg");
        String msg = service.addImages(listingId, urls);

        assertEquals("Images added successfully", msg);
        // verify that save was called for each url
        verify(listingImagesRepository, times(2)).save(any(ListingImages.class));
    }

    @Test
    void addImages_shouldThrow_whenListingNotFound() {
        String listingId = "L1";
        when(listingsRepository.findById(listingId)).thenReturn(Optional.empty());

        var ex = assertThrows(RuntimeException.class, () -> service.addImages(listingId, List.of("u")));
        assertTrue(ex.getMessage().contains("Listing not found"));
        verify(listingsRepository).findById(listingId);
        verifyNoInteractions(listingImagesRepository);
    }

    @Test
    void addImagesMultipart_shouldUploadAndPersist_whenImagesProvided() {
        String listingId = "L1";
        Listings listing = new Listings();
        listing.setId(listingId);

        when(listingsRepository.findById(listingId)).thenReturn(Optional.of(listing));

        MultipartFile mf1 = mock(MultipartFile.class);
        MultipartFile mf2 = mock(MultipartFile.class);

        when(mf1.isEmpty()).thenReturn(false);
        when(mf2.isEmpty()).thenReturn(false);

        when(cloudinaryService.uploadImage(mf1)).thenReturn("http://cloud/a.jpg");
        when(cloudinaryService.uploadImage(mf2)).thenReturn("http://cloud/b.jpg");

        String msg = service.addImagesMultipart(listingId, List.of(mf1, mf2));

        assertEquals("Images uploaded successfully", msg);
        verify(cloudinaryService).uploadImage(mf1);
        verify(cloudinaryService).uploadImage(mf2);
        verify(listingImagesRepository, times(2)).save(any(ListingImages.class));
    }

    @Test
    void addImagesMultipart_shouldSkipEmptyFiles() {
        String listingId = "L1";
        Listings listing = new Listings();
        listing.setId(listingId);

        when(listingsRepository.findById(listingId)).thenReturn(Optional.of(listing));

        MultipartFile mf1 = mock(MultipartFile.class);
        when(mf1.isEmpty()).thenReturn(true);

        String msg = service.addImagesMultipart(listingId, List.of(mf1));

        assertEquals("Images uploaded successfully", msg);
        verify(cloudinaryService, never()).uploadImage(any());
        verify(listingImagesRepository, never()).save(any());
    }

    @Test
    void addImagesMultipart_shouldThrow_whenListingNotFound() {
        String listingId = "L1";
        when(listingsRepository.findById(listingId)).thenReturn(Optional.empty());

        MultipartFile mf = mock(MultipartFile.class);
        when(mf.isEmpty()).thenReturn(false);

        var ex = assertThrows(RuntimeException.class, () -> service.addImagesMultipart(listingId, List.of(mf)));
        assertTrue(ex.getMessage().contains("Listing not found"));
        verify(listingsRepository).findById(listingId);
    }

    @Test
    void deleteImage_shouldDeleteIfExists() {
        String imageId = "IMG1";
        when(listingImagesRepository.existsById(imageId)).thenReturn(true);

        String msg = service.deleteImage(imageId);

        assertEquals("Image deleted successfully", msg);
        verify(listingImagesRepository).existsById(imageId);
        verify(listingImagesRepository).deleteById(imageId);
    }

    @Test
    void deleteImage_shouldThrowIfNotExists() {
        String imageId = "IMG1";
        when(listingImagesRepository.existsById(imageId)).thenReturn(false);

        var ex = assertThrows(RuntimeException.class, () -> service.deleteImage(imageId));
        assertTrue(ex.getMessage().contains("Image not found with ID"));
        verify(listingImagesRepository).existsById(imageId);
        verify(listingImagesRepository, never()).deleteById(any());
    }
}
