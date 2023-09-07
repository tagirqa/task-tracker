plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":task-tracker-api-v1-jackson"))
    implementation(project(":task-tracker-common"))

    testImplementation(kotlin("test-junit"))
}