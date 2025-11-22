package com.example.admin.controllers;

import com.example.admin.constants.ApiEndpoints;
import com.example.admin.dto.listings.*;
import com.example.admin.services.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoints.ADMIN_BASE)
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /** Fetch all listings */
    @GetMapping(ApiEndpoints.FETCH_ALL_LISTINGS)
    public ResponseEntity<FetchAllListingsResponse> fetchAllListings() {
        try {
            var listings = adminService.fetchAllListings();
            return ResponseEntity.ok(new FetchAllListingsResponse(listings));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(new FetchAllListingsResponse(null));
        }
    }

    /** Delete listing */
    @PostMapping(ApiEndpoints.DELETE_LISTING)
    public ResponseEntity<DeleteListingResponse> deleteListing(@RequestBody DeleteListingRequest req) {
        try {
            String msg = adminService.deleteListing(req.getListingId());
            return ResponseEntity.ok(new DeleteListingResponse(msg));

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new DeleteListingResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new DeleteListingResponse("Internal server error"));
        }
    }

    /** Mark unavailable */
    @PostMapping(ApiEndpoints.MARK_UNAVAILABLE)
    public ResponseEntity<MarkUnavailableResponse> markUnavailable(@RequestBody MarkUnavailableRequest req) {
        try {
            String msg = adminService.markUnavailable(req.getListingId());
            return ResponseEntity.ok(new MarkUnavailableResponse(msg));

        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new MarkUnavailableResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MarkUnavailableResponse("Internal server error"));
        }
    }
}
