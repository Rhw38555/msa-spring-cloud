FROM openjdk:11-ea-28-jdk-slim
VOLUME /tmp
COPY target/apigateway-service-1.0.jar ApiGatewayService.jar
ENV HOST_NAME=apigateway-service
ENTRYPOINT ["java","-jar","ApiGatewayService.jar"]

