ARG APP_NAME=match-service

FROM gradle:8.14.2-jdk21-alpine AS build
ARG APP_NAME
WORKDIR /project
COPY . .
RUN gradle :$APP_NAME:bootJar --no-daemon

FROM openjdk:21-slim
ENV SERVER_PORT=8084
ARG APP_NAME
WORKDIR /app
COPY --from=build /project/$APP_NAME/build/libs/*.jar service.jar

EXPOSE ${SERVER_PORT}
ENTRYPOINT ["sh", "-c", "java -jar /app/service.jar --server.port=${SERVER_PORT}" ]
