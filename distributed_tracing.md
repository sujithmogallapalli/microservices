## Why Do We Need Distributed Tracing?
* In microservices, a single request travels across multiple services.
* Example call chain:
```text
Client → API Gateway → Currency Conversion → Currency Exchange → Database
```
* Problems without tracing:
    * Hard to debug failures
    * Hard to identify which service is slow
    * Logs scattered across services
    * No correlation between logs
* We need:
    * End-to-end request visibility
    * Performance analysis
    * Failure tracking

## What is Distributed Tracing?
* Distributed tracing tracks a request across multiple microservices.
* Each request is assigned:
    * **Trace ID** → Unique for the entire request
    * **Span ID** → Unique per service call
* Helps visualize:
    * Service call hierarchy
    * Execution time per service
    * Bottlenecks
    * Errors

## Modern Spring Boot Tracing Stack (Boot 3+/4)
```text
Actuator
   ↓
Micrometer Tracing
   ↓
Brave (Tracing implementation)
   ↓
Zipkin (UI + Storage)
```
* Actuator → Captures observability events
* Micrometer → Vendor-neutral tracing API
* Brave → Generates trace & span IDs
* Zipkin → Stores and visualizes traces

## Add Dependencies (Spring Boot 4)
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-micrometer-tracing-brave</artifactId>
</dependency>

<dependency>
   <groupId>io.micrometer</groupId>
   <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>

<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-zipkin</artifactId>
</dependency>
```

## Application Configuration
```properties
management.tracing.sampling.probability=1.0
management.tracing.export.zipkin.endpoint=http://localhost:9411/api/v2/spans
management.tracing.export.zipkin.enabled=true

logging.pattern.level=%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]
```
* `sampling.probability=1.0` → Trace all requests (dev mode)
* Trace ID and Span ID added to logs
* Zipkin endpoint configured

## Run Zipkin
```bash
docker run -p 9411:9411 openzipkin/zipkin:2.23
```
- Access UI:
```
http://localhost:9411
```

## How It Works Internally
1. Request hits API.
2. Actuator detects the request.
3. Micrometer creates Trace ID + Span ID.
4. Brave records start & end time.
5. After request completion:
    * Trace data sent asynchronously to Zipkin.
6. Zipkin stores and displays trace.

## Tracing Across Multiple Services
* Add same tracing dependencies to:
    * Currency Exchange
    * Currency Conversion
    * API Gateway
* Trace ID is propagated automatically via HTTP headers.
* No manual coding required.

## Tracing with Feign (Spring Boot 3+)
* Add:
```xml
<dependency>
    <groupId>io.github.openfeign</groupId>
    <artifactId>feign-micrometer</artifactId>
</dependency>
```
* Enables tracing of Feign calls.
* Automatically instruments service-to-service communication.

## Using RestClient (Spring Boot 4 Recommended)
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-restclient</artifactId>
</dependency>
```
* Configuration:
```java
@Configuration(proxyBeanMethods = false)
class RestClientConfiguration {

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }
}
```
* RestTemplate is planned for deprecation.
* RestClient supports automatic tracing.

## What I Implemented
* Launched Zipkin using Docker.
* Integrated Micrometer Tracing with Brave.
* Configured trace export to Zipkin.
* Enabled traceId and spanId in logs.
* Verified trace flow across:
    * API Gateway
    * Currency Conversion
    * Currency Exchange
* Observed traces in Zipkin UI.