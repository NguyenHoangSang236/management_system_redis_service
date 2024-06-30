./mvnw clean install package spring-boot:repackage
rm splited-app-* | true
split -b 21M target/FoolishStoreProject-0.0.1-SNAPSHOT.jar splited-app-
git add .
git commit -m 'deploy'
git push origin deployment
ssh root@103.200.20.153 'cd management_system_redis_service && rm -f target/redis_service-0.0.1-SNAPSHOT.jar && git pull origin deployment && cat splited-app-* > target/redis_service-0.0.1-SNAPSHOT.jar && docker rm -f redis-service-container && docker compose up -d --build && rm -r splited-app-*'
