FROM openjdk:11-ea-28-jdk-slim
VOLUME /tmp
COPY target/catalog-service-1.0.jar CatalogService.jar
ENTRYPOINT ["java","-jar","CatalogService.jar"]