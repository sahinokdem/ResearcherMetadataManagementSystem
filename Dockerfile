#Stage 1 Build with maven
FROM maven:3.6.3-jdk-8-slim as build
WORKDIR /app
COPY pom.xml .
COPY . .
RUN mvn clean package

#Stage 2 Creating image
FROM gcr.io/distroless/java:8
WORKDIR /app
COPY --from=build /app/target/researcher-metadata-management-system.jar .
EXPOSE 8085
CMD ["java","-jar","researcher-metadata-management-system.jar"]
