#!/bin/bash

minikube start --insecure-registry="localhost:5000"

eval $(minikube docker-env)

echo "[✔] Minikube started with access to local Docker Registry"
