package com.example.ApiGateway.config;

import com.example.ApiGateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts; // Import Jwts factory
import org.junit.jupiter.api.Assertions; // Import Assertions
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private GatewayFilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Mock successful filter chain to just return empty Mono
        when(filterChain.filter(org.mockito.ArgumentMatchers.any(ServerWebExchange.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    void testApply_MissingAuthHeader_ShouldReturnUnauthorized() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/listing/1").build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter filter = jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config());

        Mono<Void> result = filter.filter(exchange, filterChain);

        StepVerifier.create(result).verifyComplete();
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void testApply_InvalidFormatHeader_ShouldReturnUnauthorized() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/listing/1")
                .header(HttpHeaders.AUTHORIZATION, "InvalidTokenFormat")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter filter = jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config());

        Mono<Void> result = filter.filter(exchange, filterChain);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void testApply_ValidToken_ShouldPassAndAddHeader() {
        String validToken = "valid.jwt.token";
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/listing/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + validToken)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // FIX: Use Jwts.claims() instead of new DefaultClaims()
        Claims claims = Jwts.claims().setSubject("user123");
        
        when(jwtUtil.validateToken(validToken)).thenReturn(claims);

        GatewayFilter filter = jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config());

        Mono<Void> result = filter.filter(exchange, filterChain);

        StepVerifier.create(result).verifyComplete();
        Assertions.assertNotEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }

    @Test
    void testApply_InvalidToken_ShouldReturnUnauthorized() {
        String invalidToken = "bad.jwt.token";
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/listing/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        when(jwtUtil.validateToken(invalidToken)).thenThrow(new RuntimeException("Invalid Signature"));

        GatewayFilter filter = jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config());

        Mono<Void> result = filter.filter(exchange, filterChain);

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
    }
}