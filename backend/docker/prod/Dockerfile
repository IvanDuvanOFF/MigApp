FROM openjdk:17-alpine
LABEL authors="Ivan Duvanov"

ENV JAVA_OPTS="-Dspring.profiles.active=dev"
VOLUME /tmp/mig-api

WORKDIR /mig-api
COPY ./build/libs/*SNAPSHOT.jar ./app.jar

EXPOSE 8080

CMD java "$JAVA_OPTS" -jar ./app.jar