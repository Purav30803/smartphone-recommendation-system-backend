plugins {
    kotlin("plugin.serialization") version "1.9.10"
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.3"
    application
}

repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(20))
    }
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.3")
    implementation("io.ktor:ktor-server-netty:2.3.3")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.3")
    implementation("org.mongodb:mongodb-driver-sync:4.10.2")
    implementation("org.litote.kmongo:kmongo-coroutine:4.11.0")
    implementation("org.litote.kmongo:kmongo:4.11.0")
    implementation("io.ktor:ktor-server-cors:2.3.3")

    implementation("org.slf4j:slf4j-simple:2.0.9")
}

application {
    mainClass.set("app.AppKt")
}
