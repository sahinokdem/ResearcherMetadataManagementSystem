
FROM fedora:latest

RUN sudo dnf install java-21-openjdk -y
RUN sudo dnf install maven -y
RUN sudo systemctl start docker
RUN sudo systemctl enable docker

WORKDIR /app

COPY ./ ./

RUN mvn \
    -Dmaven.test.skip=false \
    --batch-mode \
    package

EXPOSE 8085

CMD java -jar ./target/researcher-metadata-management-system.jar .
