package com.example.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.search.dto.SearchRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void testSearchByKeyword() throws Exception {
        SearchRequest req = new SearchRequest("iphone", null, null, null);

        mockMvc.perform(post("/api/search/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.length()").value(1))
                .andExpect(jsonPath("$.results[0].title").value("iPhone 12"));
    }

    @Test
    void testSearchByCategory() throws Exception {
        SearchRequest req = new SearchRequest(null, "electronics", null, null);

        mockMvc.perform(post("/api/search/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.length()").value(2));
    }

    @Test
    void testSearchByPriceRange() throws Exception {
        SearchRequest req = new SearchRequest(null, null, 0.0, 10000.0);

        mockMvc.perform(post("/api/search/listings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results.length()").value(1))
                .andExpect(jsonPath("$.results[0].title").value("Wooden Table"));
    }
}
