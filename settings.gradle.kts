rootProject.name = "task-tracker"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openapiVersion: String by settings

    plugins {
        id("org.openapi.generator") version openapiVersion apply false
        kotlin("jvm") version kotlinVersion apply false
    }
}

include("homework-1")
include("task-tracker-api-v1-jackson")
include("task-tracker-mappers-v1")
include("task-tracker-common")