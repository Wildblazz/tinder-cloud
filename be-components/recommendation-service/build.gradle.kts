group = "io.github.wildblazz"
version = "0.0.1-SNAPSHOT"
description = "recommendation-service"


dependencies {
    implementation(project(":shared-module"))
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}

