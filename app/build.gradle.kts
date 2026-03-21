plugins {
    id("buildsrc.convention.kotlin-jvm")
    id("org.graalvm.buildtools.native") version "0.11.1"
    application
}

dependencies {
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
            
            // Fix for Windows GitHub Actions: force temp directory to be on same drive as build
            buildArgs.add("-H:TempDirectory=" + layout.buildDirectory.dir("native-temp").get().asFile.absolutePath)
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
