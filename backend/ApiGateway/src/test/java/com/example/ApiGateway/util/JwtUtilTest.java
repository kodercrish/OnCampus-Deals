package com.example.ApiGateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String SECRET = "THIS_IS_A_VERY_SECRET_KEY_FOR_JWT_AUTHENTICATION";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void testValidateToken_Success() {
        // 1. Generate a valid token manually
        Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
        String token = Jwts.builder()
                .setSubject("testUser")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // 2. Validate it using your class
        var claims = jwtUtil.validateToken(token);

        // 3. Assertions
        assertNotNull(claims);
        assertEquals("testUser", claims.getSubject());
    }

    @Test
    void testValidateToken_InvalidSignature() {
        // Generate token with a different secret
        String wrongSecret = "WRONG_SECRET_KEY_FOR_TESTING_PURPOSES_ONLY_123";
        Key key = Keys.hmacShaKeyFor(wrongSecret.getBytes());
        String token = Jwts.builder()
                .setSubject("testUser")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Expect an exception when validating with the real secret
        assertThrows(Exception.class, () -> {
            jwtUtil.validateToken(token);
        });
    }
}