plugins {
    kotlin("jvm") version "2.0.0"
}

group = "net.bladehunt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://mvn.bladehunt.net/releases")
}

dependencies {
    testImplementation(kotlin("test"))
    compileOnly("net.minestom:minestom-snapshots:73b308673b")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}