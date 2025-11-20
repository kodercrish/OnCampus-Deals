package com.example.listing.dto.fetch;

import lombok.*;
import com.example.listing.entities.Listings;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FetchSellerListingsResponse {
    private List<Listings> listings;
}
