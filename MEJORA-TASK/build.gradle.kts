plugins {
    kotlin("jvm") version "2.0.21"
    id("org.jetbrains.dokka") version "1.9.20"
}

group = "es.prog2425.calcprueba"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:2.0.9")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}