group = "io.github.wildblazz"
version = "0.0.1-SNAPSHOT"
description = "match-service"


dependencies {
	implementation(project(":shared-module"))
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}
