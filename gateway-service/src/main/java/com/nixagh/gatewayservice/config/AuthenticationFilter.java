package com.nixagh.gatewayservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {
    private final RouterValidator routerValidator;
    private final TokenUtil tokenUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if(!routerValidator.isSecured.test(request)) return chain.filter(exchange);

        if (tokenUtil.isAuthMissing(request))
            return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

        final String token = tokenUtil.getToken(request);

        // check token in database
//        if(!tokenUtil.isWhiteList(token))
//            return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

        // check validateToken
        try {
            if (!tokenUtil.isValid(token))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
        } catch (JwtException e) {
            return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);
        }

        tokenUtil.populateRequestWithHeaders(exchange, token);

        return chain.filter(exchange);
    }

    /*PRIVATE*/

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

}