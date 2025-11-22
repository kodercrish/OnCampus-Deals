package com.example.search.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "listings")  // same table as Listing microservice
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ListingSearchView {

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

    @Column(name = "status")
    private String status;
}