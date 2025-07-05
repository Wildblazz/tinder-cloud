FROM gradle:8.14.2-jdk21-alpine AS build
WORKDIR /app
COPY . .
RUN gradle publishToMavenLocal
