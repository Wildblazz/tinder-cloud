#!/bin/bash

set -euo pipefail

apply_secret() {
  local SECRETS_DIR=$1
  local NAMESPACE=$2

  if [[ ! -f "$SECRETS_DIR/.env.secrets" || ! -f "$SECRETS_DIR/secret.yaml.template" ]]; then
    echo "Missing .env.secrets or secret.yaml.template in $SECRETS_DIR"
    exit 1
  fi

  export $(cat "$SECRETS_DIR/.env.secrets" | xargs)

  envsubst < "$SECRETS_DIR/secret.yaml.template" > "$SECRETS_DIR/secret.yaml.generated"

  kubectl create namespace "$NAMESPACE" --dry-run=client -o yaml | kubectl apply -f -

  kubectl apply -f "$SECRETS_DIR/secret.yaml.generated"
}

apply_secret argocd/base/tinder-app/secrets tinder-space
apply_secret argocd/base/infrastructure/secrets infrastructure
