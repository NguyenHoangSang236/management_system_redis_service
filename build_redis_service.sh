#!/bin/bash
./mvnw clean install
rm splited-app-* | true
split -b 21M target/redis_service-0.0.1-SNAPSHOT.jar splited-app-
git add .
git commit -m 'deploy'
git push origin deployment
ssh root@103.200.20.153 'cd management_system_redis_service && git pull origin deployment && cat splited-app-* > target/redis_service-0.0.1-SNAPSHOT.jar && docker rm -f redis-service-container && docker build -t redis-service-container . && docker run -p 8079:8079 --name redis-service-container redis-service-container && rm -r splited-app-*'