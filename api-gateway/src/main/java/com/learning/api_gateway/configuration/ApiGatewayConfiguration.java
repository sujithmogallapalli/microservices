package com.learning.api_gateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator gatewayLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(
                        predicateSpec -> predicateSpec.path("/currency-exchange/**")
                                .uri("lb://currency-exchange")
                )
                .route(
                        predicateSpec -> predicateSpec.path("/currency-conversion/**")
                                .uri("lb://currency-conversion")
                )
                .route(
                        predicateSpec -> predicateSpec.path("/currency-conversion-feign/**")
                                .uri("lb://currency-conversion")
                )
                .route(
                        predicateSpec -> predicateSpec.path("/currency-conversion-new/**")
                                .filters(gatewayFilterSpec -> gatewayFilterSpec.rewritePath(
                                        "/currency-conversion-new/(?<segment>.*)",
                                        "/currency-conversion-feign/${segment}"
                                ))
                                .uri("lb://currency-conversion")
                )
                .build();
    }

}
