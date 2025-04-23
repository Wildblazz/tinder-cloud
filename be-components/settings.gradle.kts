rootProject.name = "tinder-cloud-be-components"

include(":shared-module", ":profile-service", ":like-service")

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    repositoriesMode = RepositoriesMode.PREFER_PROJECT
}
