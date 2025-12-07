package com.example.ApiGateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    // 1. Identity Service Route
    "spring.cloud.gateway.routes[0].id=identity-service",
    "spring.cloud.gateway.routes[0].uri=http://localhost:9999",
    "spring.cloud.gateway.routes[0].predicates[0]=Path=/api/auth/**",

    // 2. Listing Service Route
    "spring.cloud.gateway.routes[1].id=listing-service",
    "spring.cloud.gateway.routes[1].uri=http://localhost:9999",
    "spring.cloud.gateway.routes[1].predicates[0]=Path=/api/listing/**",

    // 3. Chat Service Route
    "spring.cloud.gateway.routes[2].id=chat-service",
    "spring.cloud.gateway.routes[2].uri=http://localhost:9999",
    "spring.cloud.gateway.routes[2].predicates[0]=Path=/api/chat/**",

    // 4. Search Service Route
    "spring.cloud.gateway.routes[3].id=search-service",
    "spring.cloud.gateway.routes[3].uri=http://localhost:9999",
    "spring.cloud.gateway.routes[3].predicates[0]=Path=/api/search/**",

    // 5. Admin Service Route
    "spring.cloud.gateway.routes[4].id=admin-service",
    "spring.cloud.gateway.routes[4].uri=http://localhost:9999",
    "spring.cloud.gateway.routes[4].predicates[0]=Path=/api/admin/**"
})
@AutoConfigureWebTestClient
class ApiGatewayIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testIdentityRoute_ShouldBePublic() {
        // Should return 5xx because localhost:9999 is unreachable (Connection Refused)
        // This confirms the Gateway ACCEPTED the request and tried to route it.
        webTestClient.get()
                .uri("/api/auth/login")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testListingRoute_MissingToken_ShouldBeUnauthorized() {
        // Should return 401 Unauthorized immediately (Filter blocks it)
        webTestClient.get()
                .uri("/api/listing/all")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testAdminRoute_MissingToken_ShouldBeUnauthorized() {
        // Should return 401 Unauthorized immediately
        webTestClient.get()
                .uri("/api/admin/dashboard")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}