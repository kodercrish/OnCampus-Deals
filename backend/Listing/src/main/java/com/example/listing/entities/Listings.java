package com.example.listing.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "listings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Listings {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "seller_id", nullable = false, length = 36)
    private String sellerId;   // FK (User Service)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.ACTIVE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ListingImages> images = new ArrayList<>();

    public enum Status {
        ACTIVE,
        SOLD,
        UNAVAILABLE
    }
}
