plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib"))
    implementation(project(":task-tracker-common"))
    implementation(project(":task-tracker-stubs"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation(project(":task-tracker-repo-tests"))

    testImplementation(kotlin("test-junit"))
}
