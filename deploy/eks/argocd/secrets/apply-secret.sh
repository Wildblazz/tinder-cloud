#!/bin/bash

SECRETS_DIR=deploy/eks/argocd/secrets

export $(cat "$SECRETS_DIR/.env.secrets" | xargs)

envsubst < $SECRETS_DIR/secret.yaml.template > $SECRETS_DIR/secret.yaml.generated

kubectl create namespace tinder-space --dry-run=client -o yaml | kubectl apply -f -

kubectl apply -f "$SECRETS_DIR/secret.yaml.generated"
