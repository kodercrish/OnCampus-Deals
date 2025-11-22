package com.example.admin.repositories;

import com.example.admin.entities.ListingAdminView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingAdminRepository extends JpaRepository<ListingAdminView, String> {
}
