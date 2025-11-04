#!/usr/bin/env bash
set -euo pipefail

SERVICE="$1"
TAG="$2"
FILE="$3"

if [ -z "${SERVICE}" ] || [ -z "${TAG}" ] || [ -z "${FILE}" ]; then
  echo "Usage: update.sh <service> <tag> <file_path>"
  exit 1
fi

if [ ! -f "${FILE}" ]; then
  echo "Error: file not found: ${FILE}"
  exit 1
fi

echo "Updating image tag for service '${SERVICE}' to '${TAG}' in ${FILE}..."

sed -E -i.bak \
  "s|(^[[:space:]]*image:[[:space:]]+[^[:space:]]*${SERVICE}):[^[:space:]]+|\1:${TAG}|" \
  "${FILE}"

rm -f "${FILE}.bak"

if grep -qE "^[[:space:]]*image:[[:space:]]+[^[:space:]]*${SERVICE}:${TAG}\b" "${FILE}"; then
  echo "Updated ${SERVICE} to tag ${TAG}"
else
  echo "No matching image line updated for ${SERVICE}"
fi
