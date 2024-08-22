#Stage 1 Build with maven
FROM eclipse/ubuntu_jdk8_x11 as build
RUN apt-get update && apt-get install -y git
RUN chown -R 1000:1000 /app
WORKDIR /app
COPY pom.xml .
COPY . .
RUN mvn clean package

#Stage 2 Creating image
FROM eclipse-temurin:8-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/researcher-metadata-management-system.jar .
EXPOSE 8085
CMD ["java","-jar","researcher-metadata-management-system.jar"]
