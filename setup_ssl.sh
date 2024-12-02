#!/bin/bash

# Định nghĩa các biến
DOMAIN="redis.management-system.com"
ALIAS="redisManagementSystem"
PASSWORD="hoangsang236"
DEFAULT_PASSWORD="changeit"
KEYSTORE_FILE="keystore.p12"
CERT_FILE="keystore.crt"
KEYSTORE_PATH="${JAVA_HOME}/lib/security/cacerts"

# Kiểm tra xem JAVA_HOME đã được thiết lập chưa
if [ -z "$JAVA_HOME" ]; then
  echo "Error: JAVA_HOME is not set. Please set JAVA_HOME and try again."
  exit 1
fi

# Xoá alias cũ khỏi cacerts của JVM nếu tồn tại
echo "Deleting alias $ALIAS from cacerts..."
sudo $JAVA_HOME/bin/keytool -delete -alias $ALIAS -keystore $KEYSTORE_PATH -storepass $DEFAULT_PASSWORD 2>/dev/null || echo "Alias $ALIAS not found in cacerts."

# Xoá alias cũ khỏi keystore.p12 nếu tồn tại
if [ -f "$KEYSTORE_FILE" ]; then
  echo "Deleting alias $ALIAS from keystore.p12..."
  $JAVA_HOME/bin/keytool -delete -alias $ALIAS -keystore $KEYSTORE_FILE -storepass $PASSWORD 2>/dev/null || echo "Alias $ALIAS not found in keystore.p12."
  echo "Deleting existing keystore file: $KEYSTORE_FILE"
  rm -f "$KEYSTORE_FILE"
fi

# Tạo keystore mới cho domain
echo "Generating new keystore for $DOMAIN..."
$JAVA_HOME/bin/keytool -genkeypair -alias $ALIAS \
  -keyalg RSA -keysize 2048 \
  -dname "CN=$DOMAIN,OU=Sang,O=Sang,C=VI" \
  -ext "SAN=DNS:$DOMAIN" \
  -storetype PKCS12 \
  -keystore $KEYSTORE_FILE \
  -storepass $PASSWORD \
  -validity 3650

if [ $? -ne 0 ]; then
  echo "Error: Failed to generate keystore."
  exit 1
fi

# Xuất file .crt từ keystore
echo "Exporting .crt file from keystore..."
$JAVA_HOME/bin/keytool -export -alias $ALIAS \
  -file $CERT_FILE \
  -keystore $KEYSTORE_FILE \
  -storepass $PASSWORD

if [ $? -ne 0 ]; then
  echo "Error: Failed to export .crt file."
  exit 1
fi

# Import file .crt vào cacerts của JVM
echo "Importing .crt file into cacerts..."
sudo $JAVA_HOME/bin/keytool -import -alias $ALIAS \
  -keystore $KEYSTORE_PATH \
  -file $CERT_FILE \
  -storepass $DEFAULT_PASSWORD -noprompt

if [ $? -ne 0 ]; then
  echo "Error: Failed to import .crt into cacerts."
  exit 1
fi

echo "All done! Keystore and certificate setup completed for $DOMAIN."
