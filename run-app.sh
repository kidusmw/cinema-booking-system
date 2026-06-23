#!/usr/bin/env bash
set -e

docker compose up -d

cleanup() {
    echo "Stopping database..."
    docker compose down
}
trap cleanup EXIT SIGINT SIGTERM

mvn javafx:run
