group = "io.github.wildblazz"
version = "0.0.1-SNAPSHOT"
description = "reaction-service"


plugins {
	kotlin("plugin.jpa") version "2.1.20"
}

dependencies {
	implementation(project(":shared-module"))
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.postgresql:postgresql:42.7.5")
}
