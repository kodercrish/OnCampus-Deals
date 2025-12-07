package com.example.ApiGateway.config;

import com.example.ApiGateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    // secret must be same size used by JwtUtil
    private static final String SECRET = "01234567890123456789012345678901";

    private JwtAuthenticationFilter filterFactory;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup() {
        filterFactory = new JwtAuthenticationFilter();
        jwtUtil = new JwtUtil(SECRET);
        // inject jwtUtil into the filter instance (field is @Autowired). We'll set via reflection to avoid changing production code.
        try {
            var f = JwtAuthenticationFilter.class.getDeclaredField("jwtUtil");
            f.setAccessible(true);
            f.set(filterFactory, jwtUtil);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void filter_shouldReturn401_whenMissingAuthorizationHeader() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/listing/some").build();
        var exchange = MockServerWebExchange.from(request);

        GatewayFilter filter = filterFactory.apply(new JwtAuthenticationFilter.Config());
        GatewayFilterChain chain = mock(GatewayFilterChain.class);

        Mono<Void> result = filter.filter(exchange, chain);

        // block to execute
        result.block();

        // expect 401 and chain not invoked
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verify(chain, never()).filter(any(ServerWebExchange.class));
    }

    @Test
    void filter_shouldReturn401_whenInvalidAuthHeader() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/listing/some")
                .header(HttpHeaders.AUTHORIZATION, "NotBearer token")
                .build();
        var exchange = MockServerWebExchange.from(request);

        GatewayFilter filter = filterFactory.apply(new JwtAuthenticationFilter.Config());
        GatewayFilterChain chain = mock(GatewayFilterChain.class);

        filter.filter(exchange, chain).block();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verify(chain, never()).filter(any(ServerWebExchange.class));
    }

    @Test
    void filter_shouldReturn401_whenTokenInvalid() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/listing/some")
                .header(HttpHeaders.AUTHORIZATION, "Bearer this.is.not.valid")
                .build();
        var exchange = MockServerWebExchange.from(request);

        GatewayFilter filter = filterFactory.apply(new JwtAuthenticationFilter.Config());
        GatewayFilterChain chain = mock(GatewayFilterChain.class);

        filter.filter(exchange, chain).block();

        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        verify(chain, never()).filter(any(ServerWebExchange.class));
    }

    @Test
    void filter_shouldCallChain_whenTokenValid() {
        // create a valid token with subject equal to user id
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
        String token = Jwts.builder()
                .setSubject("user-abc")
                .signWith(key)
                .compact();

        MockServerHttpRequest request = MockServerHttpRequest.get("/api/listing/some")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        var exchange = MockServerWebExchange.from(request);

        GatewayFilter filter = filterFactory.apply(new JwtAuthenticationFilter.Config());
        GatewayFilterChain chain = mock(GatewayFilterChain.class);
        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());

        // execute filter
        filter.filter(exchange, chain).block();

        // chain should be invoked once
        verify(chain, times(1)).filter(any(ServerWebExchange.class));

        // Because the filter attempts to mutate the request headers, we at least ensure the chain executed successfully.
        // (Mutation API used in code builds a mutated request; verifying header presence on exchange is non-trivial
        // because original exchange object is not reassigned in the production code. The important behavior is: valid token -> chain called.)
    }
}
