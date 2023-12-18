plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":task-tracker-common"))

    // logging
    implementation(project(":task-tracker-api-log1"))
    implementation(project(":task-tracker-mapper-log1"))

    // Stubs
    implementation(project(":task-tracker-stubs"))

    // Biz
    implementation(project(":task-tracker-biz"))
}