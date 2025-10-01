#!/bin/bash

minikube start --cpus 6

eval $(minikube docker-env)

echo "[✔] Minikube started with access to local Docker Registry"
