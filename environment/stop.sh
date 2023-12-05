#!/bin/bash
BOLD_RED='\033[1;31m'
BOLD_GREEN='\033[1;32m'
BOLD_YELLOW='\033[1;33m'
NO_COLOR='\033[0m'

if ! docker info >/dev/null 2>&1; then
  printf "%bDocker isn't running. Please start docker !\n%b" "${BOLD_RED}" "${NO_COLOR}"
  exit 1
fi

if [ -n "$(docker network ls -f "name=ms-network" -q)" ]; then
  runningBeforeStop="$(docker-compose ps -a --services --filter "status=running")"
  if [ -z "$runningBeforeStop" ]; then
    printf "%bEnvironment already stopped\n%b" "${BOLD_GREEN}" "${NO_COLOR}"
    exit 0
  fi
else
  printf "%bNo environment to stop\n%b" "${BOLD_RED}" "${NO_COLOR}"
  exit 1
fi

printf "%bStopping environment...\n%b" "${BOLD_YELLOW}" "${NO_COLOR}"
docker-compose stop
running="$(docker-compose ps --services --filter "status=running")"
if [ -n "$running" ]; then
  printf "%bSome services could not be stopped. Please try again. If the problem persists, please stop each service manually\n%b" "${BOLD_RED}" "${NO_COLOR}"
  exit 1
else
  printf "%bEnvironment stopped\n%b" "${BOLD_GREEN}" "${NO_COLOR}"
fi
