# Tinder-Cloud
### Project Overview

Tinder-Cloud is a backend-focused replication of Tinder's core functionality, designed as a practice ground for advanced microservice architecture implementation. 
This project maintains focus on distributed systems challenges.
Note: This project is strictly educational in nature and intended solely for learning purposes, not for commercial use or public deployment.

### Project Purpose
This project serves as a practical platform to refine expertise in:

* Microservice orchestration in cloud environments
* Event-driven architecture implementation
* Scalable matching algorithms and recommendation systems
* Distributed data management patterns
* Oauth 2.0 Authentication and authorization 

### Dating Platform Concept
The application follows the popular swipe-based dating concept:
* User-friendly matchmaking based on mutual interest
* Location-based profile discovery
* Simple binary selection mechanism (like/dislike)
* Connection established only upon mutual interest (matching)

### Core Functionality
The system will implement these key dating platform capabilities:

* User profile management with CRUD operations
* Photo management with cloud storage integration
* Location-based profile recommendation engine
* Matching mechanism
* Real-time event processing for match notifications

### Technical Stack

* Programming language: Java/Kotlin + Spring Boot
* Authentication: Keycloak for identity management and OAuth flows
* Storage: AWS S3 for media assets
* Database: PostgreSQL as main data storage, MongoDb to speed up some operations 
* Messaging: Kafka for inter-service communication
* Caching: Redis for optimizing recommendation delivery
* Infrastructure: Container-based deployment (Docker/Kubernetes)
* CI/CD: GitHub Actions

### Architecture

- [Link to architecture documentation](docs/architecture/high-level.md)

### Local start
* Create `.env` file in the 'deploy/docker' directory. Use `.env-example` as a reference
* From directory 'deploy/docker' run command: `docker compose --env-file .env up` to start DBs, KeyCloak, Kafka, etc

### Creating docker images
* From folder /be-components run `docker build -f ${SERVICE_NAME}/Dockerfile -t match-service:v1 .` (change ${SERVICE_NAME} to real service name)
* Check Dockerfiles of each service, what env variables are expected 
* Run with expected env variables. Example: `docker run -e MONGODB_USERNAME=admin -e MONGODB_PASSWORD=password -p 8084:8084 your-image-name`

### Kubernetes deployment
* Run minikube:
  * `chmod +x deploy/eks/minikube-init.sh`
  * `./deploy/eks/minikube-init.sh`
  * Optionally open minikube UI: `minikube dashboard`
* Run ArgoCD:
  * `chmod +x deploy/eks/argocd-init.sh` 
  * `./deploy/eks/argocd-init.sh`
* Install kustomize: `brew install kustomize`
  * To make ArgoCd UI accessible - `kubectl port-forward svc/argocd-server -n argocd 8080:443`
  * ArgoCd UI: `https://localhost:8080`
  * Login: admin. Get password: `kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d` -> string without % is password
* Create ArgoCD apps:
  * `kubectl apply -f deploy/eks/argocd/app-projects/infrastructure.yaml` 
  * `kubectl apply -f deploy/eks/argocd/app-projects/tinder-cloud.yaml`
* Generate secrets and add into cluster:
  * In `/eks/argocd/secrets/` create a file `.env.secrets` Example file: `.env.secrets.example`
  * `chmod +x deploy/eks/argocd/secrets/apply-secret.sh`
  * `./deploy/eks/argocd/secrets/apply-secret.sh`
* Deploy infra apps in cluster:
  * `kubectl apply -f deploy/eks/argocd/apps/infrastructure-app.yaml`
  * `kubectl apply -f deploy/eks/argocd/apps/tinder-app.yaml`\
* Expose local docker registry port: `kubectl port-forward -n infrastructure service/registry 5000:5000` 
* Generate app images: 
  * From the folder `be-components`: `docker build -t localhost:5000/profile-service:latest -f profile-service/Dockerfile .`



Shield: [![CC BY-NC 4.0][cc-by-nc-shield]][cc-by-nc]

This work is licensed under a
[Creative Commons Attribution-NonCommercial 4.0 International License][cc-by-nc].

[![CC BY-NC 4.0][cc-by-nc-image]][cc-by-nc]

[cc-by-nc]: https://creativecommons.org/licenses/by-nc/4.0/
[cc-by-nc-image]: https://licensebuttons.net/l/by-nc/4.0/88x31.png
[cc-by-nc-shield]: https://img.shields.io/badge/License-CC%20BY--NC%204.0-lightgrey.svg
