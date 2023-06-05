FROM openjdk:11-ea-28-jdk-slim
VOLUME /tmp
COPY target/order-service-1.0.jar OrderService.jar
ENTRYPOINT ["java","-jar","OrderService.jar"]