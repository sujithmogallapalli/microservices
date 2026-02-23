## Why Do We Need a Naming Server?
- In microservices:
  - Services run on different ports 
  - Multiple instances exist 
  - Containers restart 
  - IP addresses change
- 
- Hardcoding URLs like:
```text
http://localhost:8000
```
❌ Not scalable 
❌ Not production-ready
- We need **dynamic service discovery**.

## What is a Naming Server?
- A Naming Server (Service Registry) is:
    > A central registry where all microservices register themselves and discover other services dynamically.
- In Spring Cloud, commonly used:
  - **Eureka Server**

## How Eureka Works
### Step 1: Start Eureka Server
- Add dependency:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```
- Enable in main class:
```java
@EnableEurekaServer
@SpringBootApplication
public class NamingServerApplication {
}
```
### Step 2: Register Microservices
- Add dependency:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```
- In `application.yml`:

```properties
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka
```

## What Happens at Runtime?
- Service starts
-Registers itself with Eureka
-Sends heartbeat
-If heartbeat stops → removed automatically

## Benefits
- ✅ No hardcoded URLs
- ✅ Dynamic scaling support 
- ✅ Automatic instance discovery 
- ✅ Self-healing registry

## What I Implemented

```markdown
- Created Eureka Naming Server
- Registered Currency Exchange Service
- Registered Currency Conversion Service
- Verified registration in Eureka dashboard
- Removed hardcoded URLs from Feign client
```

## Why Do We Need Load Balancing?
- If we run multiple instances:
```text
Currency Exchange Service:
  - Instance 1 (Port 8000)
  - Instance 2 (Port 8001)
  - Instance 3 (Port 8002)
```
- We need:
  - Traffic distribution 
  - High availability 
  - Better performance

## Types of Load Balancing

### Server-Side
- Load balancer like:
  - NGINX 
  - AWS ELB
### Client-Side (Spring Cloud)
- Client chooses instance using:
  - Service Discovery 
  - Load balancing algorithm
- Spring Cloud uses:
  - Spring Cloud LoadBalancer (modern)
  - Ribbon (older, deprecated)

## How to Enable Load Balancing
- Just use:
```java
@FeignClient(name = "currency-exchange")
```
- No URL required. 
- Spring Cloud LoadBalancer automatically handles distribution.

## Load Balancing Algorithm
- By default, Round Robin
- Each request goes to next instance.

## Benefits
- ✅ High availability
- ✅ Fault tolerance
- ✅ Horizontal scaling
- ✅ Better resource utilization

## What I Implemented

```markdown
- Ran multiple instances of Currency Exchange Service
- Used Feign Client for service-to-service communication
- Verified automatic instance switching
- Confirmed round-robin behavior via environment field
```