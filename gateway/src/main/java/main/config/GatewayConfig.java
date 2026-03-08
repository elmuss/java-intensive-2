package main.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f.circuitBreaker(config -> config
                                .setName("userServiceCB")
                                .setFallbackUri("forward:/fallback/user")))
                        .uri("lb://user-service")) // Используем имя из Eureka
                .route("notification-service", r -> r.path("/notifications/**")
                        .filters(f -> f.circuitBreaker(config -> config
                                .setName("notificationServiceCB")
                                .setFallbackUri("forward:/fallback/notification")))
                        .uri("lb://notification-service"))
                .build();
    }
}
