package com.example.listing.dto.fetch;

import lombok.*;
import com.example.listing.entities.Listings;
import com.example.listing.entities.ListingImages;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FetchListingResponse {
    private String message;
    private Listings listing;
    private List<ListingImages> images;
}
