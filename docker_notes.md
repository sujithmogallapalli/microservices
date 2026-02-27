## What is Microservices Architecture?
- Microservices architecture is an approach where:
  * Application is divided into **small, focused services**
  * Each service owns its **own database**
  * Services are independently deployable
  * Different services can use different programming languages

### Example Architecture:
* MovieService → DB1
* CustomerService → DB2
* ReviewService → DB3
* BookingService → DB4
* FareCalculationService → DB5

### Key Advantages
* Independent scaling
* Independent deployment
* Technology flexibility (Java, Go, Python, NodeJS)
* Better fault isolation

### Challenge
- As services increase:
  * Deployment becomes complex
  * Managing environments becomes difficult
  * Need standard way to deploy services
- Solution → **Containers**

## Traditional Deployment Model
### Steps in Traditional Deployment
- Operations team manually:
  1. Setup hardware
  2. Install OS
  3. Install runtime (Java, Python, NodeJS)
  4. Setup dependencies
  5. Install application
### Problems
* Time-consuming
* Error prone
* Environment mismatch issues
* Hard to replicate setup

## Container-Based Deployment (Docker)
### Simplified Deployment Model
- With Docker:
  1. Developer creates Docker Image
  2. Ops runs Docker container
- Key Idea:
    > "Build once, run anywhere"
- OS, language, hardware differences do not matter.
## How Docker Works
### Docker Image Contains
* OS layer
* Application runtime (JDK, Python, etc.)
* Application code
* Dependencies

## Docker Container
- Runtime instance of Docker image.

## Why Docker is Popular
### 1. Standardized Packaging
- Same packaging for:
  * Java
  * Python
  * NodeJS
### 2. Multi Platform Support
- Runs on:
  * Local machine
  * Data center
  * Cloud (AWS, Azure, GCP)
### 3. Isolation
- Containers are isolated from each other.

## Docker Terminology
### Docker Image
- A packaged version of an application including OS + runtime + dependencies.

### Docker Container
- Running instance of an image.

### Docker Registry
- Storage location for Docker images.

### Docker Hub
- Public Docker registry.

### Docker Repository
- Collection of images for a specific app (with tags).

### Dockerfile
- File containing instructions to build a Docker image.

## Running Docker Container
- Example:
```
docker container run -d -p 5000:5000 in28min/hello-world-nodejs:0.0.1.RELEASE
```
### Explanation
* `-d` → Detached mode
* `-p hostPort:containerPort`
* `in28min/hello-world-nodejs` → Repository
* `0.0.1.RELEASE` → Tag/version

## Dockerfile – Basic Version
```
FROM openjdk:18.0-slim
COPY target/*.jar app.jar
EXPOSE 5000
ENTRYPOINT ["java","-jar","/app.jar"]
```
### Instructions
* FROM → Base image
* COPY → Copy jar
* EXPOSE → Container port
* ENTRYPOINT → Startup command

## Multi-Stage Dockerfile (Recommended)
```
FROM maven:3.8.6-openjdk-18-slim AS build
WORKDIR /home/app
COPY . /home/app
RUN mvn clean package

FROM openjdk:18.0-slim
EXPOSE 5000
COPY --from=build /home/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```
### Why Multi-Stage?
* Build happens inside container
* No dependency on local machine
* Smaller final image

## Docker Layer Caching Optimization
- Best practice:
  * Copy `pom.xml` first
  * Run dependency download
  * Then copy source code
- Improves build performance.

## Spring Boot Maven Plugin for Docker
- Useful commands:
```
mvn spring-boot:repackage
mvn spring-boot:run
mvn spring-boot:build-image
```
### Benefits
* Build executable jar
* Run app
* Create container image without writing Dockerfile (optional)

## Why Containers Matter in Microservices
- Without containers:
  * Environment mismatch
  * Complex deployments
  * Manual steps

- With containers:
  * Immutable infrastructure
  * Consistent environments
  * Easy scaling
  * Cloud portability