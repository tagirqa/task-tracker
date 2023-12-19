plugins {
    kotlin("jvm")
}

dependencies {
    val datetimeVersion: String by project

    implementation(project(":task-tracker-api-log1"))
    implementation(project(":task-tracker-common"))
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")
    testImplementation(project(":task-tracker-api-log1"))
    testImplementation(kotlin("test-junit"))
}