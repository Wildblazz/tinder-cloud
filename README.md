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
- <a href="/docs/architecture/high-level-cloud-architecture-1.eraserdiagram" data-element-id="Eho4AljltyBMTzARm7xV0"><img src="/.eraser/4wgZFFj3vFgristM8iP4___1PC2Z3FLoVY1RStR2M9qaWJj2Yr1___---diagram----96973410c27f931c620674109d830973.png" alt="" data-element-id="Eho4AljltyBMTzARm7xV0" /></a>

### Local start in docker
* Create `.env` file in the 'deploy/docker' directory. Use `.env-example` as a reference
* From directory 'deploy/docker' run command: `docker compose --env-file .env up` to start DBs, KeyCloak, Kafka, etc

### Creating docker images
* From folder /be-components run `docker build -f ${SERVICE_NAME}/Dockerfile -t match-service:v1 .` (change ${SERVICE_NAME} to real service name)
* Check Dockerfiles of each service, what env variables are expected 
* Run with expected env variables. Example: `docker run -e MONGODB_USERNAME=admin -e MONGODB_PASSWORD=password -p 8084:8084 your-image-name`

### Kubernetes deployment
* Clone gitops repo: [Tinder-Cloud-GitOps](https://github.com/Wildblazz/tinder-cloud-gitops.git)
* Use instructions in the [README](https://github.com/Wildblazz/tinder-cloud-gitops/blob/main/README.md#quick-start)

* Generate app images: 
  * By script: `chmod +x deploy/scripts/build-images.sh` and `./deploy/scripts/build-images.sh`
  * Or manually:
    * Make images accessible inside minikube BEFORE image building: `eval $(minikube docker-env)`
    * profile-service:        `docker build -t profile-service:latest -f be-components/profile-service/Dockerfile .`
    * reaction-service:       `docker build -t reaction-service:latest -f be-components/reaction-service/Dockerfile .`
    * recommendation-service: `docker build -t recommendation-service:latest -f be-components/recommendation-service/Dockerfile .`
    * match-service:          `docker build -t match-service:latest -f be-components/match-service/Dockerfile .`
`
Shield: [![CC BY-NC 4.0][cc-by-nc-shield]][cc-by-nc]

This work is licensed under a
[Creative Commons Attribution-NonCommercial 4.0 International License][cc-by-nc].

[![CC BY-NC 4.0][cc-by-nc-image]][cc-by-nc]

[cc-by-nc]: https://creativecommons.org/licenses/by-nc/4.0/
[cc-by-nc-image]: https://licensebuttons.net/l/by-nc/4.0/88x31.png
[cc-by-nc-shield]: https://img.shields.io/badge/License-CC%20BY--NC%204.0-lightgrey.svg
