
FROM fedora:latest

FROM maven:3.8.6-eclipse-temurin-11-alpine AS maven
RUN apk update && apk add git && apk add net-tools procps openssh-client openssh-server
RUN mkdir -p $HOME/images/lib/ && cd $HOME/images/lib/

WORKDIR /app

COPY ./ ./

RUN mvn \
    -Dmaven.test.skip=false \
    --batch-mode \
    package

EXPOSE 8085

CMD java -jar ./target/researcher-metadata-management-system.jar .
