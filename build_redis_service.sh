./mvnw clean install package spring-boot:repackage
sudo docker rm -f redis_service_container
sudo docker build --tag=redis_service_container .
sudo docker run -p 8079:8079 --name redis_service_container redis_service_container