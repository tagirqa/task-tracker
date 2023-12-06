plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val cache4kVersion: String by project
    val coroutinesVersion: String by project
    val kmpUUIDVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
    implementation(project(":task-tracker-common"))
    implementation("com.benasher44:uuid:$kmpUUIDVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation(project(":task-tracker-repo-tests"))

    testImplementation(kotlin("test-junit"))
}
