## Why Do We Need Feign?
- In microservices, services communicate with each other using REST APIs.
- Earlier approach:
```java
RestTemplate restTemplate = new RestTemplate();
ResponseEntity<ExchangeValue> response =
    restTemplate.getForEntity(url, ExchangeValue.class);
```
- Problems with RestTemplate:
  - Boilerplate code
  - Manual URL construction
  - Hardcoded service URLs
  - Difficult to maintain 
  - No built-in load balancing (without extra config)
## What is Feign Client?
- Feign is a **declarative REST client** provided by Spring Cloud.
- Instead of writing HTTP call logic manually, you just define an interface.
- Spring generates the implementation at runtime.
## Add Dependency
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
## Enable Feign in Main Class
```java
@SpringBootApplication
@EnableFeignClients
public class CurrencyConversionApplication {
}
```
## Create Feign Interface
- Instead of writing RestTemplate logic:
```java
@FeignClient(name = "currency-exchange")
public interface CurrencyExchangeProxy {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    ExchangeValue retrieveExchangeValue(
            @PathVariable String from,
            @PathVariable String to);
}
```
## Use Feign in Service
```java
@Autowired
private CurrencyExchangeProxy proxy;

CurrencyConversion exchangeValue = proxy.retrieveExchangeValue(from, to);
```
- That’s it. No URL building. No RestTemplate.
## How It Works Internally
- Feign:
  1. Creates dynamic proxy implementation.
  2. Uses HTTP client under the hood.
  3. Converts method calls → HTTP calls.
  4. Deserializes JSON response automatically.

## What I Implemented
- Implemented service-to-service communication using RestTemplate
- Refactored communication using Feign Client