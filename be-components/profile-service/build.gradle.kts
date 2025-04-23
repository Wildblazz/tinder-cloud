group = "io.github.wildblazz"
version = "0.0.1-SNAPSHOT"
description = "profile-service"


dependencies {
	implementation(project(":shared-module"))
	implementation("io.minio:minio:8.5.17")
}
