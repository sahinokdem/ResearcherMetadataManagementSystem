#Stage 1 Build with maven
FROM maven:3.9.6-openjdk-8-slim as build
WORKDIR /app
COPY pom.xml .
COPY . .
RUN mvn clean package -DskipTests

#Stage 2 Creating image
FROM eclipse-temurin:8-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/researcher-metadata-management-system.jar .
EXPOSE 8085
CMD ["java","-jar","researcher-metadata-management-system.jar"]
