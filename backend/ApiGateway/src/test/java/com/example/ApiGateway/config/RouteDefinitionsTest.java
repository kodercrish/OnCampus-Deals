package com.example.ApiGateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies that route definitions from application.properties are loaded into the RouteDefinitionLocator.
 * This confirms your properties-based routing configuration is being parsed at startup.
 */
@SpringBootTest
@ActiveProfiles("test")
class RouteDefinitionsTest {

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    @Test
    void routeDefinitions_shouldContainExpectedRouteIds() {
        List<RouteDefinition> defs = routeDefinitionLocator.getRouteDefinitions()
                .collectList()
                .block();

        assertNotNull(defs, "Route definitions must not be null");

        List<String> ids = defs.stream().map(RouteDefinition::getId).collect(Collectors.toList());

        // These IDs should match the ones you set in application.properties
        assertTrue(ids.contains("identity-service"), "identity-service route must be present");
        assertTrue(ids.contains("listing-service"), "listing-service route must be present");
        assertTrue(ids.contains("chat-service"), "chat-service route must be present");
        assertTrue(ids.contains("search-service"), "search-service route must be present");
        assertTrue(ids.contains("admin-service"), "admin-service route must be present");

        // Optional: inspect one route's predicates / URIs for sanity
        RouteDefinition listing = defs.stream().filter(d -> "listing-service".equals(d.getId())).findFirst().orElse(null);
        assertNotNull(listing, "listing-service route should exist");
        assertFalse(listing.getPredicates().isEmpty(), "listing-service must have predicates");
        assertFalse(listing.getFilters().isEmpty() || listing.getFilters()==null, "listing-service should have filters (e.g., StripPrefix, JwtAuthenticationFilter)");
    }
}
