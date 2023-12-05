#!/bin/bash
read -p "Are you sure you want to remove the environment ? (y/n) " -n 1 -r
printf "\n"
if [[ $REPLY =~ ^[Yy]$ ]]; then

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
    if [ -n "$runningBeforeStop" ]; then
      sh stop.sh
    fi
    stoppedBeforeRemove="$(docker-compose ps -a --services --filter "status=exited")"
    if [ -z "$stoppedBeforeRemove" ]; then
      printf "%bEnvironment already removed\n%b" "${BOLD_GREEN}" "${NO_COLOR}"
      exit 0
    fi
  else
    printf "%bNo environment to remove\n%b" "${BOLD_RED}" "${NO_COLOR}"
    exit 1
  fi

  printf "%bRemoving environment...\n%b" "${BOLD_YELLOW}" "${NO_COLOR}"
  docker network rm ms-network
  docker-compose rm -f
  stopped="$(docker-compose ps --services --filter "status=exited")"
  if [ -n "$stopped" ]; then
    printf "%bSome services could not be removed. Please try again. If the problem persists, please remove each service manually\n%b" "${BOLD_RED}" "${NO_COLOR}"
    exit 1
  else
    printf "%bEnvironment removed\n%b" "${BOLD_GREEN}" "${NO_COLOR}"
  fi
fi
