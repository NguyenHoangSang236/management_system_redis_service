./mvnw clean install package spring-boot:repackage
sudo docker rm -f redis-service-container
sudo docker build -t redis-service-container .
sudo docker run -p 8079:8079 --name redis-service-container redis-service-container