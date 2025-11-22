package com.example.admin.dto.listings;

import lombok.*;
import com.example.admin.entities.ListingAdminView;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FetchAllListingsResponse {
    private List<ListingAdminView> listings;
}
