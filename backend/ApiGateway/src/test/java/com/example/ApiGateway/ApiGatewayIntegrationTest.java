package com.example.ApiGateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration-style tests for routing + filters.
 *
 * Notes:
 * - Downstream target is localhost:9999 (intentionally unreachable) so a 5xx shows gateway attempted to forward.
 * - Protected routes include JwtAuthenticationFilter in the test route definitions; missing Authorization header should yield 401.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    // NOTE: using the newer server.webflux namespace to avoid migration warnings
    // 1. Identity Service Route (public)
    "spring.cloud.gateway.server.webflux.routes[0].id=identity-service",
    "spring.cloud.gateway.server.webflux.routes[0].uri=http://localhost:9999",
    "spring.cloud.gateway.server.webflux.routes[0].predicates[0]=Path=/api/auth/**",
    "spring.cloud.gateway.server.webflux.routes[0].filters[0]=StripPrefix=0",

    // 2. Listing Service Route (secured)
    "spring.cloud.gateway.server.webflux.routes[1].id=listing-service",
    "spring.cloud.gateway.server.webflux.routes[1].uri=http://localhost:9999",
    "spring.cloud.gateway.server.webflux.routes[1].predicates[0]=Path=/api/listing/**",
    "spring.cloud.gateway.server.webflux.routes[1].filters[0]=StripPrefix=0",
    "spring.cloud.gateway.server.webflux.routes[1].filters[1]=JwtAuthenticationFilter",

    // 3. Chat Service Route (secured)
    "spring.cloud.gateway.server.webflux.routes[2].id=chat-service",
    "spring.cloud.gateway.server.webflux.routes[2].uri=http://localhost:9999",
    "spring.cloud.gateway.server.webflux.routes[2].predicates[0]=Path=/api/chat/**",
    "spring.cloud.gateway.server.webflux.routes[2].filters[0]=StripPrefix=0",
    "spring.cloud.gateway.server.webflux.routes[2].filters[1]=JwtAuthenticationFilter",

    // 4. Search Service Route (secured)
    "spring.cloud.gateway.server.webflux.routes[3].id=search-service",
    "spring.cloud.gateway.server.webflux.routes[3].uri=http://localhost:9999",
    "spring.cloud.gateway.server.webflux.routes[3].predicates[0]=Path=/api/search/**",
    "spring.cloud.gateway.server.webflux.routes[3].filters[0]=StripPrefix=0",
    "spring.cloud.gateway.server.webflux.routes[3].filters[1]=JwtAuthenticationFilter",

    // 5. Admin Service Route (secured)
    "spring.cloud.gateway.server.webflux.routes[4].id=admin-service",
    "spring.cloud.gateway.server.webflux.routes[4].uri=http://localhost:9999",
    "spring.cloud.gateway.server.webflux.routes[4].predicates[0]=Path=/api/admin/**",
    "spring.cloud.gateway.server.webflux.routes[4].filters[0]=StripPrefix=0",
    "spring.cloud.gateway.server.webflux.routes[4].filters[1]=JwtAuthenticationFilter",

    // Provide JWT secret so JwtUtil can be created in context
    "jwt.secret=01234567890123456789012345678901"
})
@AutoConfigureWebTestClient
class ApiGatewayIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testIdentityRoute_ShouldBePublic() {
        // Downstream is unreachable -> gateway attempts to forward -> 5xx expected
        webTestClient.get()
                .uri("/api/auth/login")
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void testListingRoute_MissingToken_ShouldBeUnauthorized() {
        // Route has JwtAuthenticationFilter -> missing Authorization header => 401
        webTestClient.get()
                .uri("/api/listing/all")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void testAdminRoute_MissingToken_ShouldBeUnauthorized() {
        webTestClient.get()
                .uri("/api/admin/dashboard")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}
