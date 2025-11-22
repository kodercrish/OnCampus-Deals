package com.example.admin.dto.listings;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MarkUnavailableRequest {
    private String listingId;
}
