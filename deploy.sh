#!/bin/bash

docker pull "${DOCKERHUB_USERNAME}/${DOCKER_IMAGE}"

mkdir -p /app/mig-api
mkdir -p /tmp/mig-api

cd /app/mig-api

CONTAINER_NAME=${DOCKER_IMAGE}
if [ "$(docker ps -qa -f name="$CONTAINER_NAME")" ]; then
    if [ "$(docker ps -q -f name="$CONTAINER_NAME")" ]; then
        echo "Container is running -> stopping it..."
        docker stop $CONTAINER_NAME;
    fi
fi

docker compose up