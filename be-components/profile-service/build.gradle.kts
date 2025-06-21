group = "io.github.wildblazz"
version = "0.0.1-SNAPSHOT"
description = "profile-service"

plugins {
    kotlin("plugin.jpa") version "2.1.20"
}

dependencies {
    implementation(project(":shared-module"))
    implementation("io.minio:minio:8.5.17")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql:42.7.5")
}
