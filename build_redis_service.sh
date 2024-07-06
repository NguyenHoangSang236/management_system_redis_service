./mvnw clean install package spring-boot:repackage
rm splited-app-* | true
split -b 21M target/redis_service-0.0.1-SNAPSHOT.jar splited-app-
git add .
git commit -m 'deploy'
git push origin deployment
ssh root@103.200.20.153 << 'EOF'
cd management_system_redis_service
git pull origin deployment
rm -r target/redis_service-0.0.1-SNAPSHOT.jar
cat splited-app-* > rm -r
docker rm -f redis-service-container
docker build -t redis-service-container .
docker run -p 8079:8079 --name redis-service-container redis-service-container
rm -r splited-app-*
EOF