plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    application
}

group = "com.catalog"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.swiftzer.semver:semver:2.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
    implementation("com.akuleshov7:ktoml-core:0.5.1")
    implementation("com.akuleshov7:ktoml-file:0.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

application {
    mainClass.set("MainKt") // Assuming your main function is in Main.kt
}