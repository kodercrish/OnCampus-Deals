package com.example.listing.dto.status;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MarkUnavailableRequest {
    private String listingId;
}
