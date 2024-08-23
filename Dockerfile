
FROM eclipse/ubuntu_jdk8

RUN dnf install java-21-openjdk -y

RUN dnf install maven -y

WORKDIR /app

COPY ./ ./

RUN mvn \
    -Dmaven.test.skip=true \
    --batch-mode \
    package

EXPOSE 8085

CMD java -jar ./target/researcher-metadata-management-system.jar .
