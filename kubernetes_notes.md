# Kubernetes & Container Orchestration
## Why Do We Need Container Orchestration?
- When running microservices in production, we usually have:
  * Multiple services
  * Multiple instances of each service
  * Containers running on different servers
- Example requirement:
  * 10 instances of Service A
  * 15 instances of Service B
  * Automatic scaling when traffic increases
- Managing this manually is impossible.
- Problems without orchestration:
  * No auto scaling
  * Hard to manage multiple containers
  * No automatic recovery
  * Manual deployments
  * Difficult service discovery
- Solution → **Container Orchestration**

## What is Container Orchestration?
- Container orchestration tools manage containers automatically.
- They handle:
  * Deployment
  * Scaling
  * Load balancing
  * Health monitoring
  * Self-healing
- Typical features:
  * **Auto Scaling**
    Increase or decrease container instances based on traffic.
  * **Service Discovery**
    Microservices automatically find each other.
    * **Load Balancing**
      Traffic is distributed across multiple instances.
  * **Self Healing**
    If a container crashes, it automatically restarts.
  * **Zero Downtime Deployment**
    New versions are deployed without stopping the application.

## Container Orchestration Options
### Cloud Provider Specific
- Examples:
    - AWS:
      * ECS
      * Fargate
    - Azure:
      * AKS
    - Google Cloud:
      * GKE

### Cloud Neutral Solution
- Most popular solution: **Kubernetes**
  * Open source
  * Works on any cloud
  * Industry standard orchestration platform

## What is Kubernetes?
- Kubernetes (K8s) is the most widely used container orchestration platform.
- It manages:
  * Container deployment
  * Scaling
  * Networking
  * Service discovery
  * Failover recovery
- Important capabilities:
  * Auto Scaling
  * Service Discovery
  * Load Balancing
  * Self Healing
  * Rolling Deployments

## Kubernetes Architecture
- A Kubernetes cluster contains:

### 1. Master Node (Control Plane)
- Responsible for managing the cluster.
- Components:
  * **API Server**
    * Entry point for all Kubernetes commands.
  * **Scheduler**
    * Decides which node should run a pod.
  * **Controller Manager**
    * Maintains desired state (replicas, deployments).
  * **etcd**
    * Distributed database storing cluster state.

### 2. Worker Nodes
- Worker nodes run applications.
- Components:
  - **Pods**
    * Smallest deployable unit.
  - **Kubelet**
    * Communicates with master node.

## What is a Pod?
- A **Pod** is the smallest deployable unit in Kubernetes.
- A pod contains:
  * One or more containers
  * Shared networking
  * Shared storage
- Important characteristics:
  * Each pod has its own IP address
  * Containers inside a pod share:
    * Network
    * Storage
    * Ports
- Pod states:
  * Running
  * Pending
  * Succeeded
  * Failed
  * Unknown

## Deployment vs ReplicaSet
### Deployment
- A deployment manages a microservice release.
- Responsibilities:
  * Maintain desired number of instances
  * Handle rolling updates
  * Manage replica sets
- Example command:
```
kubectl create deployment hello-world-rest-api --image=in28min/hello-world-rest-api:0.0.1.RELEASE
```

### ReplicaSet
- ReplicaSet ensures a specific number of pods are always running.
- Example:
```
kubectl scale deployment hello-world-rest-api --replicas=3
```
- If one pod crashes:
    - ReplicaSet automatically creates a new pod.

## Kubernetes Service
- Each pod has a dynamic IP.
- Problem:
  - When pods restart, their IP changes.
- Solution → **Kubernetes Service**
- A service provides:
  * Stable endpoint
  * Load balancing
  * Service discovery

## Types of Kubernetes Services

### ClusterIP
* Default type
* Service accessible only inside the cluster
* Use case:
  * Internal microservice communication

### NodePort
* Exposes service on each node IP
* Accessible outside cluster
* Example:
```
http://NodeIP:NodePort
```

### LoadBalancer
* Uses cloud provider load balancer
* Exposes service externally
* Example:
```
kubectl expose deployment hello-world-rest-api --type=LoadBalancer --port=8080
```
## Kubernetes Deployment YAML
- Example deployment configuration:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hello-world-rest-api
spec:
  replicas: 3
  selector:
    matchLabels:
      app: hello-world-rest-api
  template:
    metadata:
      labels:
        app: hello-world-rest-api
    spec:
      containers:
        - name: hello-world-rest-api
          image: in28min/hello-world-rest-api:0.0.3.RELEASE
```
- This configuration:
  * Deploys container image
  * Runs 3 replicas
  * Automatically manages pods.

## Kubernetes Service YAML
- Example service configuration:
```yaml
apiVersion: v1
kind: Service
metadata:
  name: hello-world-rest-api
spec:
  selector:
    app: hello-world-rest-api
  ports:
    - port: 8080
      targetPort: 8080
  type: LoadBalancer
```
- This exposes the microservice externally.

## Health Checks in Kubernetes
- Kubernetes continuously checks service health using **probes**.
- Two important probes:

### Readiness Probe
- Checks if application is ready to receive traffic.
- If readiness fails:
- Traffic is **not sent to the pod**.
- Example:
```
/actuator/health/readiness
```

### Liveness Probe
- Checks if application is alive.
- If liveness fails:
  - Kubernetes **restarts the pod**.
- Example:
```
/actuator/health/liveness
```
- Spring Boot Actuator provides these endpoints.

## Autoscaling in Kubernetes
- Kubernetes supports **Horizontal Pod Autoscaling (HPA)**.
- It increases or decreases pods based on CPU usage.
- Example:
```
kubectl autoscale deployment currency-exchange --min=1 --max=3 --cpu-percent=5
```
- This means:
  * Minimum pods = 1
  * Maximum pods = 3
  * Scale when CPU > 5%

## What I Implemented in This Course
### 1. Run Spring Boot container
```
docker run -p 8080:8080 in28min/hello-world-rest-api:0.0.1.RELEASE
```
### 2. Deploy application to Kubernetes
```
kubectl create deployment hello-world-rest-api --image=in28min/hello-world-rest-api:0.0.1.RELEASE
```
### 3. Expose application
```
kubectl expose deployment hello-world-rest-api --type=LoadBalancer --port=8080
```
### 4. Verify resources
```
kubectl get pods
kubectl get services
kubectl get deployments
```
### 5. Scale application
```
kubectl scale deployment hello-world-rest-api --replicas=3
```
### 6. Configure microservices deployment
- Deployed:
  * currency-exchange service
  * currency-conversion service
```
kubectl create deployment currency-exchange --image=in28min/mmv3-currency-exchange-service
kubectl create deployment currency-conversion --image=in28min/mmv3-currency-conversion-service
```

### 7. Service discovery using Kubernetes services
```
kubectl expose deployment currency-exchange --type=LoadBalancer --port=8000
```

# Additional Kubernetes Features Implemented
### ConfigMap (Centralized Configuration)
- Example:
```
kubectl create configmap currency-conversion --from-literal=CURRENCY_EXCHANGE_URI=http://currency-exchange
```
- Used for:
  * External configuration
  * Environment variables

### Logging & Monitoring
- Enabled:
  * Cloud Logging
  * Distributed tracing
- This helps:
  * Debug microservice communication
  * Monitor system health