package com.nixagh.gatewayservice.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
@Component
@RequiredArgsConstructor
public class TokenUtil {
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    public boolean isWhiteList(String token) {
        String token_id = restTemplate.postForObject("http://localhost:9004/tokens/isWhiteList", token, String.class);
        return token_id != null;
    }

    public String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    public String getToken(ServerHttpRequest request) {
        return getAuthHeader(request).replace("Bearer ", "");
    }

    public boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    public void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("id")))
                .header("role", String.valueOf(claims.get("role")))
                .build();
    }
    public boolean isValid(String token) {
        return jwtUtil.validateToken(token);
    }
}
