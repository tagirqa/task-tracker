import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
}

group = "ru.otus.otuskotlin.tasktracker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

subprojects {
    group = rootProject.group
    version = rootProject.version
    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
}