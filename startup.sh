#!/bin/bash

set -e 

BOOK_SERVICE_PORT_1=9901
BOOK_SERVICE_PORT_2=9902
BOOK_SERVICE_PORT_3=9903

LOAN_SERVICE_PORT_1=9801
LOAN_SERVICE_PORT_2=9802


docker compose up -d

cd registry-service
mvn spring-boot:run > /dev/null & 
cd ..

cd api-gateway 
mvn spring-boot:run > /dev/null & 
cd ..

cd book-service
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=$BOOK_SERVICE_PORT_1  &
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=$BOOK_SERVICE_PORT_2  &
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=$BOOK_SERVICE_PORT_3  &
cd ..


cd loan-service
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=LOAN_SERVICE_PORT_1  &
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=LOAN_SERVICE_PORT_2  &
cd ..
