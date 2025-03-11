## build stage

## Chọn ảnh base aka runtime enviroment cho code chạy
FROM maven:3.9.9-amazoncorretto-17 AS build

## Chọn directory chứa code
WORKDIR /app

## Copy code ở vị trí hiện tại (dấu chấm thứ nhất) vào current workdir trong container
COPY . .

RUN mvn install -DskipTests=true

## run stage
FROM openjdk:17-jdk-slim

WORKDIR /run
COPY --from=build /app/target/library-0.0.1-SNAPSHOT.jar /run/library-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/run/library-0.0.1-SNAPSHOT.jar"]

