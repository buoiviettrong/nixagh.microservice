package com.nixagh.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
public class GatewayConfig {
    private final AuthenticationFilter filter;
    private final LogoutHandler logoutHandler;

    public GatewayConfig(AuthenticationFilter filter, LogoutHandler logoutHandler) {
        this.filter = filter;
        this.logoutHandler = logoutHandler;
    }
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/users/**")
                        .uri("http://localhost:9002")
                )

                .route("user-service", r -> r
                        .path("/tokens/**")
                        .uri("http://localhost:9002")
                )
//                .route("user-service", r -> r
//                        .path("/tokens/**")
//                        .filters(f -> f.filter(filter))
//                        .uri("http://localhost:9002")
//                )
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("http://localhost:9004")
                )

                .route("image-service", r -> r
                        .path("/images/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8801")
                )
                .build();
    }

}
