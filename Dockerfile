#Stage 1 Build with maven
FROM eclipse/ubuntu_jdk8 as build
RUN sudo apt-get update && sudo apt-get install -y openjdk-17-jdk maven git
RUN sudo chown -R 1000:1000 /app /app/target # /app ve /app/target dizinlerinin sahipliğini ve izinlerini ayarlayın
WORKDIR /app
COPY pom.xml .
COPY . .
RUN mvn -X clean package 

#Stage 2 Creating image
FROM eclipse-temurin:17-jdk-alpine 
WORKDIR /app
COPY --from=build /app/target/researcher-metadata-management-system.jar .
EXPOSE 8085
CMD ["java","-jar","researcher-metadata-management-system.jar"]