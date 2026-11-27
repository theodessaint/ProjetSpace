#!/usr/bin/env bash

set -e

# Récupérer le chemin absolu du répertoire du script
SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"

BACK_DIR="${SCRIPT_DIR}/../space-station-back"
FRONT_DIR="${SCRIPT_DIR}/../space-station-front"

echo "Répertoire courant : $(pwd)"
echo "Répertoire du script : ${SCRIPT_DIR}"
echo "Backend : ${BACK_DIR}"
echo "Frontend : ${FRONT_DIR}"

# Build du backend
cd "${BACK_DIR}"
echo Build du backend ....
./mvnw clean package -DskipTests

# Build du frontend
cd "${FRONT_DIR}"
echo Build du frontend ....
npm ci
ng build

# Construction des images et lancement des conteneurs Docker
cd "${SCRIPT_DIR}"
echo Lancement de docker compose ....
docker compose up --build -d