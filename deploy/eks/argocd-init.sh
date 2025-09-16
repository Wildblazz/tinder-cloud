#!/bin/bash

kubectl create namespace argocd

kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

kubectl port-forward svc/argocd-server -n argocd 8444:443 &

sudo kubectl -n ingress-kong port-forward service/kong-proxy 80:80 &
