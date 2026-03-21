plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    id("buildsrc.convention.kotlin-jvm")

    // Apply the Application plugin to add support for building an executable JVM application.
    application

    // GraalVM Native Image plugin
    id("org.graalvm.buildtools.native") version "0.11.1"
}

dependencies {
    // Project "app" depends on project "utils". (Project paths are separated with ":", so ":utils" refers to the top-level "utils" project.)
    // implementation("io.github.prule.acc.client:acc-client:1.0-SNAPSHOT")
    implementation("com.github.prule:acc-client:main-SNAPSHOT")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
}

application {
    // Define the Fully Qualified Name for the application main class
    mainClass.set("io.github.prule.acc.client.example.AppKt")
}

graalvmNative {
    binaries {
        named("main") {
            // Main options
            imageName.set("acc-client-example")
            mainClass.set("io.github.prule.acc.client.example.AppKt")

            // Advanced options
            buildArgs.add("--verbose")
            // fallback = false means the build will fail if it can't be fully native (no JVM fallback)
            fallback.set(false)
        }
    }
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
