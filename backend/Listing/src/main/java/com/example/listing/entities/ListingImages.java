package com.example.listing.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "listing_images")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ListingImages {
    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "listing_id", nullable = false)
    @JsonBackReference
    private Listings listing;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl; // Cloudinary URL
}