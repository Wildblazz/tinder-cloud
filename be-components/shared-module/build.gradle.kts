group = "io.github.wildblazz"
version = "0.0.1-SNAPSHOT"
description = "shared-module"

dependencies {
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}
