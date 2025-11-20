package com.example.listing.dto.images;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AddImagesRequest {
    private String listingId;
    private List<String> imageUrls;
}
