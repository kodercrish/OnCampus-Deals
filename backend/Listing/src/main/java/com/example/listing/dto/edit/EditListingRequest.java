package com.example.listing.dto.edit;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EditListingRequest {
    private String listingId;
    private String title;
    private String description;
    private Double price;
    private String category;
}
