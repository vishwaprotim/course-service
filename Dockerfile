FROM openjdk:21
LABEL authors="protim"

WORKDIR /app
COPY ./build/libs/course-service-0.0.1.jar /app
EXPOSE 8080

CMD["java", "-jar", "course-service-0.0.1.jar"]