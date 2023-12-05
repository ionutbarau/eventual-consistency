#!/bin/bash
BOLD_RED='\033[1;31m'
BOLD_GREEN='\033[1;32m'
BOLD_YELLOW='\033[1;33m'
NO_COLOR='\033[0m'

if ! docker info > /dev/null 2>&1; then
  printf "%bDocker isn't running. Please start docker !\n%b" "${BOLD_RED}" "${NO_COLOR}"
  exit 1
fi

if [ -n "$(docker network ls -f "name=ms-network" -q )" ]; then
  runningBeforeUp="$(docker-compose ps -a --services --filter "status=running")"
  servicesBeforeUp="$(docker-compose ps -a --services)"
  if [ "$runningBeforeUp" == "$servicesBeforeUp" ]; then
    printf "%bEnvironment already started\n%b" "${BOLD_GREEN}" "${NO_COLOR}"
    exit 0
  fi
fi

printf "%bStarting environment...\n%b" "${BOLD_YELLOW}" "${NO_COLOR}"
docker-compose up -d --no-recreate
running="$(docker-compose ps --services --filter "status=running")"
services="$(docker-compose ps --services)"
if [ "$running" != "$services" ]; then
  printf "%bSome services could not be started. Stopping environment...\n%b" "${BOLD_RED}" "${NO_COLOR}"
  docker-compose stop
  exit 1
else
  docker exec -d mongodb1 mongo --eval "var cfg = {_id : 'local-mongodb-replica-set',members: [{ _id : 0, host : 'mongodb1:27017', priority:2 },{ _id : 1, host : 'mongodb2:27018', priority:1 }]};rs.initiate(cfg, { force: true });"

  TIMEOUT=60
  until curl --output /dev/null --silent --fail localhost:8080; do
    if [[ $counter -eq 0 ]]; then
      printf "%bWaiting for Kafka UI%b" "${BOLD_YELLOW}" "${NO_COLOR}"
    fi
    printf '.'
    sleep 1
    if [[ $counter -eq ${TIMEOUT} ]]; then
      printf "\n%bEnvironment failed to start because Kafka UI is not reachable after $TIMEOUT seconds. Please try again\n" "${RED}"
      printf "%bStopping environment...\n%b" "${BOLD_RED}" "${NO_COLOR}"
      docker-compose stop
      exit 1
    fi
    counter=$((counter + 1))
  done
  if [[ $counter -ne 0 ]]; then
    printf "\n"
  fi
  printf "%bEnvironment started\n%b" "${BOLD_GREEN}" "${NO_COLOR}"
  exit 0
fi
