package com.example.ApiGateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.cors.reactive.CorsWebFilter;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {CorsConfig.class})
class CorsConfigTest {

    @Autowired
    private CorsWebFilter corsWebFilter;

    @Test
    void corsWebFilterBeanExists() {
        assertNotNull(corsWebFilter);
    }
}
