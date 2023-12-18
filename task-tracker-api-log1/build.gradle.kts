import org.gradle.kotlin.dsl.resolver.buildSrcSourceRootsFilePath

plugins {
    kotlin("jvm")
    id("org.openapi.generator")
    kotlin("plugin.serialization")
}

sourceSets {
    main {
        java.srcDir("$buildDir/generate-resources/main/src/main/kotlin")
    }
}

dependencies {
    val coroutinesVersion: String by project
    val serializationVersion: String by project


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
    // https://mvnrepository.com/artifact/com.squareup.moshi/moshi
    implementation("com.squareup.moshi:moshi:1.14.0")

}

openApiGenerate {
    val openapiGroup = "${rootProject.group}.api.logs"
    generatorName.set("kotlin") // Это и есть активный генератор
    packageName.set(openapiGroup)
    apiPackage.set("$openapiGroup.api")
    modelPackage.set("$openapiGroup.models")
    invokerPackage.set("$openapiGroup.invoker")
    inputSpec.set("$rootDir/specs/specs-task-log.yaml")

    /**
     * Здесь указываем, что нам нужны только модели, все остальное не нужно
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    /**
     * Настройка дополнительных параметров из документации по генератору
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions.set(mapOf(
        "dateLibrary" to "string",
        "enumPropertyNaming" to "UPPERCASE",
        "collectionType" to "list"
    ))
}

tasks {
    filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(this.openApiGenerate)
    }
}