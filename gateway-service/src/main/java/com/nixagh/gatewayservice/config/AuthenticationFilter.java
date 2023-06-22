package com.nixagh.gatewayservice.config;

import com.google.common.collect.Lists;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {
    private final String[] ignorePath = new String[] {
            "/auth/",
            "/users/login",
            "/auth/register",
            "/auth/authenticate"
    };

    private final RouterValidator routerValidator;

    private final JwtUtil jwtUtil;

    public AuthenticationFilter(RouterValidator routerValidator, JwtUtil jwtUtil) {
        this.routerValidator = routerValidator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if(!routerValidator.isSecured.test(request)) return chain.filter(exchange);

        if (this.isAuthMissing(request))
            return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

        final String token = this.getToken(request);

        // check token in database


        // check validateToken
        try {
            if (!jwtUtil.validateToken(token))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
        } catch (JwtException e) {
            return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
        }

        this.populateRequestWithHeaders(exchange, token);

        return chain.filter(exchange);
    }

    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private String getToken(ServerHttpRequest request) {
        return getAuthHeader(request).replace("Bearer ", "");
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("id")))
                .header("role", String.valueOf(claims.get("role")))
                .build();
    }
}