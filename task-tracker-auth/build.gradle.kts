plugins {
    kotlin("jvm")
}

val coroutinesVersion: String by project
val datetimeVersion: String by project

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    api("org.jetbrains.kotlinx:kotlinx-datetime:$datetimeVersion")

    implementation(project(":task-tracker-common"))
}