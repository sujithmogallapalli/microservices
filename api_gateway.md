# Spring Cloud API Gateway

## Why Do We Need an API Gateway?
- In microservices:
  - Multiple services 
  - Multiple URLs 
  - Clients shouldn’t directly talk to services 
  - Cross-cutting concerns needed
- Problems without Gateway:
  - Client must know all service URLs 
  - Authentication repeated in every service 
  - No centralized logging 
  - No unified routing

## What is Spring Cloud Gateway?
- Spring Cloud Gateway is:
    > A reactive API Gateway built on top of Spring WebFlux.
- It acts as:
  - Single entry point 
  - Router 
  - Filter 
  - Security layer

## Key Features
- ✅ Route APIs dynamically
- ✅ Integrates with Eureka (Service Discovery)
- ✅ Load balancing support
- ✅ Path rewriting
- ✅ Filters (Global & Route-specific)
- ✅ Logging
- ✅ Security integration
- ✅ Reactive & non-blocking

## Steps
### Step 1 – Create API Gateway
- On Spring Initializer:
  - Artifact: `api-gateway`
    - Dependencies:
      * DevTools
      * Actuator
      * Eureka Discovery Client
      * **Reactive Gateway (Important)**

---

## Custom Route Configuration
- Instead of automatic discovery, define routes manually.
- Created:
```java
@Configuration
public class ApiGatewayConfiguration {
```

### Route Configuration
```java
@Bean
public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
    return builder.routes()

        .route(p -> p.path("/currency-exchange/**")
                .uri("lb://currency-exchange"))

        .route(p -> p.path("/currency-conversion/**")
                .uri("lb://currency-conversion"))

        .route(p -> p.path("/currency-conversion-feign/**")
                .uri("lb://currency-conversion"))

        .route(p -> p.path("/currency-conversion-new/**")
                .filters(f -> f.rewritePath(
                        "/currency-conversion-new/(?<segment>.*)",
                        "/currency-conversion-feign/${segment}"))
                .uri("lb://currency-conversion"))

        .build();
}
```
#### Important Concepts Here

- lb://
- `lb://currency-exchange`
- Means:
  * Load-balanced call
  * Uses Eureka to resolve service
  * Uses Spring Cloud LoadBalancer
- Path Predicate
```java
.path("/currency-exchange/**")
```
    - Matches incoming request path.
- Path Rewriting
```java
.rewritePath(
"/currency-conversion-new/(?<segment>.*)",
"/currency-conversion-feign/${segment}")
```
- This rewrites:
```
/currency-conversion-new/...
```
to

```
/currency-conversion-feign/...
```

### Global Logging Filter
- Created:

```java
@Component
public class LoggingFilter implements GlobalFilter {
```
- Implementation
```java
@Override
public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    logger.info("Path of the request received -> {}", 
            exchange.getRequest().getPath());
    return chain.filter(exchange);
}
```
#### What is GlobalFilter?
- GlobalFilter:
  * Runs for every request
  * Used for:
      * Logging
      * Security
      * Rate limiting
      * Monitoring

## Why Reactive Gateway?
- Spring Cloud Gateway is built on:
  * Spring WebFlux
  * Reactive programming
  * Non-blocking I/O
- Benefits:
  * High throughput
  * Low resource usage
  * Better performance under load

## What I Implemented

```markdown
- Created Spring Cloud API Gateway
- Registered it with Eureka
- Enabled Discovery Locator
- Defined custom routes using RouteLocator
- Implemented path rewriting
- Integrated load balancing via lb://
- Implemented a Global Logging Filter
- Verified routing to multiple microservices
```