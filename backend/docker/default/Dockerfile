FROM openjdk:21
LABEL authors="Ivan Duvanov"

ENV JAVA_OPTS="-Dspring.profiles.active=default"
COPY . .
RUN ./gradlew bootJar

EXPOSE 8080

CMD java "$JAVA_OPTS" -jar ./build/libs/*SNAPSHOT.jar