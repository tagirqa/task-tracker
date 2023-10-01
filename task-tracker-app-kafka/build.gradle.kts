plugins {
    kotlin("jvm")
}

dependencies {
    val kafkaVersion: String by project
    val coroutinesVersion: String by project
    val atomicfuVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project
    implementation("org.apache.kafka:kafka-clients:$kafkaVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:atomicfu:$atomicfuVersion")

    // log
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    implementation(project(":task-tracker-common"))
    // transport models
    implementation(project(":task-tracker-common"))
    implementation(project(":task-tracker-api-v1-jackson"))
    implementation(project(":task-tracker-mappers-v1"))
    // logic
    implementation(project(":task-tracker-biz"))

    testImplementation(kotlin("test-junit"))
}