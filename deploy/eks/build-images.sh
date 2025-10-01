#!/bin/bash

eval $(minikube docker-env)

docker build -t profile-service:latest        -f be-components/profile-service/Dockerfile . &
docker build -t reaction-service:latest       -f be-components/reaction-service/Dockerfile . &
docker build -t recommendation-service:latest -f be-components/recommendation-service/Dockerfile . &
docker build -t match-service:latest          -f be-components/match-service/Dockerfile . &
wait

echo "[✔] Images have been built and available in Minikube"
