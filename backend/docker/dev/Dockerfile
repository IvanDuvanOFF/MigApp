FROM gradle:8.7-jdk17-alpine AS build

LABEL authors="Ivan Duvanov"

RUN mkdir /mig-api

COPY . /project
WORKDIR /project

RUN gradle clean build -x test

FROM amazoncorretto:17-alpine-jdk AS run

RUN mkdir /mig-api
RUN addgroup -g 1001 ivangroup && \
    adduser -S ivan -u 1001

COPY --from=build /project/build/libs/*SNAPSHOT.jar /mig-api/app.jar

VOLUME /tmp/mig-app/api

ENV JAVA_OPTS="-Dspring.profiles.active=dev"

EXPOSE 8080

CMD java "$JAVA_OPTS" -jar /mig-api/app.jar