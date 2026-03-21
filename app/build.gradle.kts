plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    // Project "app" depends on project "utils". (Project paths are separated with ":", so ":utils" refers to the top-level "utils" project.)
    implementation("io.github.prule.acc.client:acc-client:1.0-SNAPSHOT")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "io.github.prule.acc.client.app.AppKt"
}

tasks.register<JavaExec>("runAccSimulator") {
    group = "application"
    mainClass.set("io.github.prule.acc.client.simulator.AccSimulatorKt")
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runSampleApp") {
    group = "application"
    mainClass.set("io.github.prule.acc.client.example.AppKt")
    classpath = sourceSets["main"].runtimeClasspath
}
