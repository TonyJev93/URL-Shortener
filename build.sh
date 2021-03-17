#!/bin/bash

echo "========== START BUILD =========="

echo "mvn clean"
mvn clean && echo "Success!!!" || { echo "What the Fail"; exit 99; }

echo "mvn install"
mvn install && echo "Success!!!" || { echo "What the Fail"; exit 99; }

echo "docker-compose down"
docker-compose down
if [ $? -ne 0 ];then
    echo "docker rm -f $(docker ps -f ancestor=tonyjev/urlshortener -q)"
    docker rm -f $(docker ps -f ancestor=tonyjev/urlshortener -q)
fi

echo "docker rmi $(docker images -f reference=tonyjev/urlshortener -q)"
docker rmi $(docker images -f reference=tonyjev/urlshortener -q)

echo "docker build -t tonyjev/urlshortener ."
docker build -t tonyjev/urlshortener . && echo "Success!!!" || { echo "What the Fail"; exit 99; }

echo "========== END BUILD =========="
