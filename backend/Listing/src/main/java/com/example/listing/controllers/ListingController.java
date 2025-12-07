package com.example.listing.controllers;

import com.example.listing.constants.ApiEndpoints;

import com.example.listing.dto.create.*;
import com.example.listing.dto.edit.*;
import com.example.listing.dto.delete.*;
import com.example.listing.dto.fetch.*;
import com.example.listing.dto.status.*;
import com.example.listing.dto.images.*;
import com.example.listing.services.ListingServices;
import com.example.listing.services.ListingImageServices;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@RestController
@RequestMapping(ApiEndpoints.LISTING_BASE)
@RequiredArgsConstructor
public class ListingController {

    private final ListingServices listingServices;
    private final ListingImageServices listingImageServices;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** Create listing + multipart images (accepts 'data' part as JSON string) */
    @PostMapping(
        value = ApiEndpoints.CREATE_LISTING,
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE },
        produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<CreateListingResponse> createListing(
            @RequestPart("data") String data,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {
        try {
            // parse data JSON -> CreateListingRequest
            CreateListingRequest request = objectMapper.readValue(data, CreateListingRequest.class);

            log.info("CreateListing called: sellerId={}, title={}, imageCount={}",
                     request.getSellerId(), request.getTitle(),
                     images == null ? 0 : images.size());

            String listingId = listingServices.createListingWithImages(request, images == null ? List.of() : images);

            return ResponseEntity.ok(new CreateListingResponse("Listing created successfully", listingId));
        } catch (RuntimeException e) {
            log.warn("Client error: {}", e.getMessage(), e);
            return ResponseEntity.status(400).body(new CreateListingResponse(e.getMessage(), null));
        } catch (Exception e) {
            log.error("Server error creating listing", e);
            return ResponseEntity.status(500).body(new CreateListingResponse("Internal server error", null));
        }
    }

    /**
     * Optional: separate upload endpoint for two-step flows:
     * POST /listing/{listingId}/upload-images
     * Consumes multipart form-data with images parts.
     */
    @PostMapping(
        value = "/{listingId}/upload-images",
        consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }
    )
    public ResponseEntity<AddImagesResponse> uploadImages(
            @PathVariable("listingId") String listingId,
            @RequestPart(value = "images", required = true) List<MultipartFile> images
    ) {
        try {
            log.info("Upload images called for listing={}, count={}", listingId, images.size());
            String msg = listingImageServices.addImagesMultipart(listingId, images);
            return ResponseEntity.ok(new AddImagesResponse(msg));
        } catch (RuntimeException e) {
            log.warn("Client error during image upload: {}", e.getMessage(), e);
            return ResponseEntity.status(400).body(new AddImagesResponse(e.getMessage()));
        } catch (Exception e) {
            log.error("Server error uploading images", e);
            return ResponseEntity.status(500).body(new AddImagesResponse("Internal server error"));
        }
    }

    /** Edit listing */
    @PostMapping(ApiEndpoints.EDIT_LISTING)
    public ResponseEntity<EditListingResponse> editListing(@RequestBody EditListingRequest request) {
        try {
            String msg = listingServices.editListing(
                request.getListingId(),
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.getCategory()
            );

            return ResponseEntity.ok(new EditListingResponse(msg));

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new EditListingResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new EditListingResponse("Internal server error"));
        }
    }

    /** Delete listing */
    @PostMapping(ApiEndpoints.DELETE_LISTING)
    public ResponseEntity<DeleteListingResponse> deleteListing(@RequestBody DeleteListingRequest request) {
        try {
            String msg = listingServices.deleteListing(request.getListingId());
            return ResponseEntity.ok(new DeleteListingResponse(msg));

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new DeleteListingResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new DeleteListingResponse("Internal server error"));
        }
    }

    /** Fetch listing with images */
    @PostMapping(ApiEndpoints.FETCH_LISTING)
    public ResponseEntity<FetchListingResponse> fetchListing(@RequestBody FetchListingRequest request) {
        try {
            var listing = listingServices.fetchListing(request.getListingId());
            var images = listingImageServices.fetchListingImages(request.getListingId());

            return ResponseEntity.ok(
                new FetchListingResponse("Listing fetched successfully", listing, images)
            );

        } catch (RuntimeException e) {
            return ResponseEntity.status(400)
                    .body(new FetchListingResponse(e.getMessage(), null, null));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new FetchListingResponse("Internal server error", null, null));
        }
    }

    /** Fetch listings by seller */
    @PostMapping(ApiEndpoints.FETCH_SELLER_LISTINGS)
    public ResponseEntity<FetchSellerListingsResponse> fetchSellerListings(
            @RequestBody FetchSellerListingsRequest request
    ) {
        try {
            var listings = listingServices.fetchListingsBySeller(request.getSellerId());
            return ResponseEntity.ok(new FetchSellerListingsResponse(listings));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new FetchSellerListingsResponse(null));
        }
    }

    /** Fetch active listings (feed) */
    @GetMapping(ApiEndpoints.FETCH_ACTIVE_LISTINGS)
    public ResponseEntity<FetchSellerListingsResponse> fetchFeed() {
        try {
            var listings = listingServices.fetchActiveListings();
            return ResponseEntity.ok(new FetchSellerListingsResponse(listings));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new FetchSellerListingsResponse(null));
        }
    }

    /** Mark as sold */
    @PostMapping(ApiEndpoints.MARK_SOLD)
    public ResponseEntity<MarkStatusResponse> markSold(@RequestBody MarkSoldRequest request) {
        try {
            String msg = listingServices.markAsSold(request.getListingId());
            return ResponseEntity.ok(new MarkStatusResponse(msg));

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new MarkStatusResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MarkStatusResponse("Internal server error"));
        }
    }

    /** Mark as unavailable */
    @PostMapping(ApiEndpoints.MARK_UNAVAILABLE)
    public ResponseEntity<MarkStatusResponse> markUnavailable(@RequestBody MarkUnavailableRequest request) {
        try {
            String msg = listingServices.markAsUnavailable(request.getListingId());
            return ResponseEntity.ok(new MarkStatusResponse(msg));

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new MarkStatusResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MarkStatusResponse("Internal server error"));
        }
    }

    /** Add images (URLs already uploaded externally or via another call) */
    @PostMapping(ApiEndpoints.ADD_IMAGES)
    public ResponseEntity<AddImagesResponse> addImages(@RequestBody AddImagesRequest request) {
        try {
            String msg = listingImageServices.addImages(request.getListingId(), request.getImageUrls());
            return ResponseEntity.ok(new AddImagesResponse(msg));

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new AddImagesResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AddImagesResponse("Internal server error"));
        }
    }

    /** Delete image */
    @PostMapping(ApiEndpoints.DELETE_IMAGE)
    public ResponseEntity<DeleteImageResponse> deleteImage(@RequestBody DeleteImageRequest request) {
        try {
            String msg = listingImageServices.deleteImage(request.getImageId());
            return ResponseEntity.ok(new DeleteImageResponse(msg));

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new DeleteImageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new DeleteImageResponse("Internal server error"));
        }
    }
}
