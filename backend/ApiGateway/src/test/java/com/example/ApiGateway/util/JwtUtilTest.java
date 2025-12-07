package com.example.ApiGateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    // must match length requirements used by JwtUtil (>= 32 bytes for HS algorithms)
    private static final String SECRET = "01234567890123456789012345678901";

    @Test
    void validateToken_shouldReturnClaims_whenTokenValid() {
        // create key and token using same secret
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
        String token = Jwts.builder()
                .setSubject("user-123")
                .signWith(key)
                .compact();

        JwtUtil jwtUtil = new JwtUtil(SECRET);
        var claims = jwtUtil.validateToken(token);

        assertNotNull(claims);
        assertEquals("user-123", claims.getSubject());
    }

    @Test
    void validateToken_shouldThrow_whenTokenInvalid() {
        JwtUtil jwtUtil = new JwtUtil(SECRET);

        String badToken = "this.is.not.a.jwt";

        assertThrows(Exception.class, () -> jwtUtil.validateToken(badToken));
    }
}
