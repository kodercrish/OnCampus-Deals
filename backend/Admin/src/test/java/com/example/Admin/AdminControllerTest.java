package com.example.admin.controllers;

import com.example.admin.dto.listings.DeleteListingRequest;
import com.example.admin.dto.listings.MarkUnavailableRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testFetchAllListings() throws Exception {
        mockMvc.perform(get("/api/admin/fetch-all-listings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listings.length()").value(3));
    }

    @Test
    void testDeleteListing() throws Exception {
        DeleteListingRequest req = new DeleteListingRequest("L1");

        mockMvc.perform(post("/api/admin/delete-listing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Listing deleted successfully"));
    }

    @Test
    void testMarkUnavailable() throws Exception {
        MarkUnavailableRequest req = new MarkUnavailableRequest("L2");

        mockMvc.perform(post("/api/admin/mark-unavailable")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Listing marked as unavailable"));
    }
}
