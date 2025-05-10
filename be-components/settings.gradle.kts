rootProject.name = "tinder-cloud-be-components"

include(":shared-module", ":profile-service", ":reaction-service", ":recommendation-service", ":match-service")

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    repositoriesMode = RepositoriesMode.PREFER_PROJECT
}
