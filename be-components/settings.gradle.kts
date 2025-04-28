rootProject.name = "tinder-cloud-be-components"

include(":shared-module", ":profile-service", ":reaction-service")

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    repositoriesMode = RepositoriesMode.PREFER_PROJECT
}
