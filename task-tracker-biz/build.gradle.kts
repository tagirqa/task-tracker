plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib-common"))
    implementation(project(":task-tracker-common"))
    implementation(project(":task-tracker-stubs"))

    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("test-junit"))

}