plugins {
    kotlin("jvm") version "1.8.21"
    application
    kotlin("plugin.serialization") version "1.8.21"
}

group = "dev.joseluisgs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    dependencies {
        // Client/Server
        implementation("io.rsocket.kotlin:rsocket-core:0.15.4")
        // TCP ktor client/server transport
        implementation("io.rsocket.kotlin:rsocket-transport-ktor-tcp:0.15.4")

        // Para hacer el logging
        implementation("io.github.microutils:kotlin-logging:3.0.4")
        implementation("ch.qos.logback:logback-classic:1.4.5")

        // Corrutinas
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")

        // Serializa Json con Kotlin
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}