# Circuit Breaker & Resilience4j
## Why Do We Need Circuit Breaker?
- In microservices:
```text
Service A → Service B → Service C → Service D
```
- If Service C:
  * Is slow
  * Is down
  * Is overloaded
- Entire chain gets impacted
- Threads block
- System crashes
- This is called:
    > Cascading Failure

## What is Circuit Breaker Pattern?
- Circuit Breaker:
  * Monitors service calls
  * Stops calling failing service
  * Returns fallback response
  * Tries again after cooldown period
- Just like electrical circuit breaker:
  * Closed → Normal operation
  * Open → Stop calls
  * Half-open → Test if service recovered

## Resilience4j
- Resilience4j is:
    > A lightweight fault tolerance library designed for functional programming.
- Supports:
  * Retry
  * Circuit Breaker
  * Rate Limiter
  * Bulkhead
  * TimeLimiter

## What Retry Does?
- If API fails:
  * Retry up to 5 times
  * Wait 1 second between attempts
  * Uses exponential backoff
- Good for temporary failures.

## Step 28 – Circuit Breaker
```java
@CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse")
```
### Configuration
```properties
resilience4j.circuitbreaker.instances.default.failureRateThreshold=90
```
- If 90% of calls fail:
  * Circuit opens
  * Calls immediately fail
  * Fallback returned
  * No call to downstream service

## Circuit Breaker States
| State     | Meaning                      |
| --------- | ---------------------------- |
| CLOSED    | Normal                       |
| OPEN      | Stop calling service         |
| HALF_OPEN | Testing if service recovered |

## Rate Limiter
```java
@RateLimiter(name="default")
```
- Configuration:
```properties
resilience4j.ratelimiter.instances.default.limitForPeriod=2
resilience4j.ratelimiter.instances.default.limitRefreshPeriod=10s
```
- Meaning:
  * Only 2 requests allowed
  * Per 10 seconds
- If exceeded → request rejected.

## Bulkhead
```java
@Bulkhead(name="sample-api")
```
- Configuration:
```properties
resilience4j.bulkhead.instances.sample-api.maxConcurrentCalls=10
```
- Meaning:
  * Only 10 concurrent calls allowed
  * Prevents thread exhaustion
- Bulkhead isolates failures.

| Feature         | Purpose                    |
| --------------- | -------------------------- |
| Retry           | Retry temporary failures   |
| Circuit Breaker | Prevent cascading failures |
| Rate Limiter    | Limit request rate         |
| Bulkhead        | Limit concurrent calls     |
| Fallback        | Return safe response       |

## What I Implemented

```markdown
- Added Resilience4j dependency
- Implemented Retry with fallback
- Configured exponential backoff
- Implemented Circuit Breaker
- Implemented Rate Limiter
- Implemented Bulkhead isolation
- Verified behavior through repeated failures
```