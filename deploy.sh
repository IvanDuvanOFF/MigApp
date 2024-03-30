#!/bin/bash

start=$(date +"%s")

ssh ${SERVER_USER}@${SERVER_HOST} -i key.txt -t -t -o StrictHostKeyChecking=no << 'ENDSSH'
docker pull "${DOCKER_USER}/${DOCKER_IMAGE}"

mkdir -p /app/mig-api
mkdir -p /tmp/mig-api

cd /app/mig-api

CONTAINER_NAME=${DOCKER_IMAGE}
if [ "$(docker ps -qa -f name=$CONTAINER_NAME)" ]; then
    if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        echo "Container is running -> stopping it..."
        docker stop $CONTAINER_NAME;
    fi
fi

docker compose up

exit
ENDSSH

if [ $? -eq 0 ]; then
  exit 0
else
  exit 1
fi

end=$(date +"%s")

diff=$(($end - $start))

echo "Deployed in : ${diff}s"