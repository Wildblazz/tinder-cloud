#!/bin/bash
set -e

SERVICE="$1"
TAG="$2"
FILE="$3"

if [ -z "$SERVICE" ] || [ -z "$TAG" ] || [ -z "$FILE" ]; then
  echo "Usage: update.sh <service> <tag> <file_path>"
  exit 1
fi

if [ ! -f "$FILE" ]; then
  echo "❌ Error: file not found: $FILE"
  exit 1
fi

echo "Updating image for service '$SERVICE' to tag '$TAG' in $FILE..."

sed -i.bak -E "s|^(\\s*image:\\s*${SERVICE}:).*|\\1${TAG}|" "$FILE"
rm -f "${FILE}.bak"

echo "✅ Updated $SERVICE to tag $TAG in $FILE"
