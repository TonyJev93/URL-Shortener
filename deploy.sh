#!/bin/bash

echo "========== START DEPLOY =========="

echo "docker-compose up -d"
docker-compose up -d && echo "Success!!!" || { echo "What the Fail"; exit 99; }

echo "========== END DEPLOY =========="
