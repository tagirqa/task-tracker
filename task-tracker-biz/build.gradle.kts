plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    val kotlinCorVersion: String by project
    val coroutinesVersion: String by project

    implementation(kotlin("stdlib-common"))
    implementation(project(":task-tracker-common"))
    implementation(project(":task-tracker-stubs"))
    implementation("com.crowdproj:kotlin-cor:$kotlinCorVersion")

    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("test-junit"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

}