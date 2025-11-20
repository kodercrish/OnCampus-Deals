package com.example.listing.dto.create;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateListingRequest {
    private String title;
    private String description;
    private Double price;
    private String category;
    private String sellerId;
}