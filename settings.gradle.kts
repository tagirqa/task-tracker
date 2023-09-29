rootProject.name = "task-tracker"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openapiVersion: String by settings
    val springframeworkBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val pluginSpringVersion: String by settings
    val pluginJpa: String by settings

    plugins {
        id("org.openapi.generator") version openapiVersion apply false
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.springframework.boot") version springframeworkBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version pluginSpringVersion apply false
        kotlin("plugin.jpa") version pluginJpa apply false
    }
}

include("homework-1")
include("task-tracker-api-v1-jackson")
include("task-tracker-mappers-v1")
include("task-tracker-common")
include("task-tracker-app-spring")
include("task-tracker-stubs")
include("task-tracker-biz")
include("task-tracker-app-kafka")