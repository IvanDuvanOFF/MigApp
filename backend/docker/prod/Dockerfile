FROM amazoncorretto:21-alpine-jdk AS run

RUN mkdir /mig-api
RUN addgroup -g 1001 ivangroup && \
    adduser -S ivan -u 1001

COPY ./backend/build/libs/*SNAPSHOT.jar /mig-api/app.jar

#VOLUME /tmp/mig-api

ENV JAVA_OPTS="-Dspring.profiles.active=dev"

EXPOSE 8080

CMD java "$JAVA_OPTS" -jar /mig-api/app.jar