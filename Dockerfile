
FROM eclipse/ubuntu_jdk8

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven

WORKDIR /app

COPY ./ ./

RUN mvn \
    -Dmaven.test.skip=true \
    --batch-mode \
    package

EXPOSE 8085

CMD java -jar ./target/researcher-metadata-management-system.jar .
