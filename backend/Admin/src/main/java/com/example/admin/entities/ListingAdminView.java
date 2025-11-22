package com.example.admin.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "listings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ListingAdminView {

    @Id
    @Column(length = 36)
    private String id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double price;

    private String category;

    @Column(name = "seller_id")
    private String sellerId;

    private String status; // ACTIVE, SOLD, UNAVAILABLE
}
