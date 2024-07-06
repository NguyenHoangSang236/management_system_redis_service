#!/bin/bash

# Build project và tạo file JAR
./mvnw clean install

# Kiểm tra xem file JAR có được tạo ra hay không
if [ ! -f target/redis_service-0.0.1-SNAPSHOT.jar ]; then
  echo "Build failed: JAR file not found."
  exit 1
fi

# Xóa các file chia tách cũ (nếu có)
rm -f splited-app-* || true

# Chia tách file JAR thành các file nhỏ hơn
split -b 21M target/redis_service-0.0.1-SNAPSHOT.jar splited-app-

# Thêm và commit các file chia tách vào git
git add .
git commit -m 'deploy'
git push origin deployment

# Kết nối SSH và thực hiện các lệnh trên máy đích
ssh root@103.200.20.153 << 'EOF'
cd management_system_redis_service
git pull origin deployment

# Kiểm tra xem các file chia tách có tồn tại hay không
if [ ! -f splited-app-aa ]; then
  echo "Deploy failed: Split files not found."
  exit 1
fi

# Tạo thư mục target nếu chưa tồn tại
mkdir -p target

# Xoá JAR file
rm /target/redis_service-0.0.1-SNAPSHOT.jar

# Hợp lại các file chia tách thành file JAR
cat splited-app-* > target/redis_service-0.0.1-SNAPSHOT.jar

# Kiểm tra xem file JAR đã hợp lại có tồn tại hay không
if [ ! -f target/redis_service-0.0.1-SNAPSHOT.jar ]; then
  echo "Deploy failed: Reassembled JAR file not found."
  exit 1
fi

# Xóa container cũ, build và run container mới
docker rm -f redis-service-container
docker build -t redis-service-container .
docker run -p8079:8079 redis-service-container

# Xóa các file chia tách
rm -r splited-app-*
EOF
