plugins {
  id("buildsrc.convention.kotlin-jvm")
  id("org.graalvm.buildtools.native") version "0.11.1"
  application
}

dependencies {
  api("com.github.prule:acc-client:main-SNAPSHOT")
  api("com.github.prule:acc-messages:main-SNAPSHOT")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
  implementation("ch.qos.logback:logback-classic:1.5.32")
  implementation("com.github.doyaaaaaken:kotlin-csv-jvm:0.15.2")
  implementation("io.github.xn32:json5k:0.3.0")
}

application {
  // Define the Fully Qualified Name for the application main class
  mainClass.set("com.github.prule.acc.client.example.AppKt")
}

graalvmNative {
  binaries {
    named("main") {
      // Main options
      imageName.set("acc-client-example")
      mainClass.set("com.github.prule.acc.client.example.AppKt")

      // Advanced options
      buildArgs.add("--verbose")
      // fallback = false means the build will fail if it can't be fully native (no JVM fallback)
      fallback.set(false)

      // Fix for Windows "different root" issue: use a temp dir inside the build folder
      buildArgs.add(
          "-H:TempDirectory=" + layout.buildDirectory.dir("native-temp").get().asFile.absolutePath
      )
    }
  }
}

// Ensure the temp directory exists and set JVM property for the native image builder process
tasks.withType<org.graalvm.buildtools.gradle.tasks.BuildNativeImageTask>().configureEach {
  val tempDir = layout.buildDirectory.dir("native-temp").get().asFile

  // Ensure directory exists before task execution starts (crucial for macOS)
  doFirst {
    if (!tempDir.exists()) {
      tempDir.mkdirs()
    }
  }

  // Set JVM system property for the builder process
  options.get().systemProperty("java.io.tmpdir", tempDir.absolutePath)
}

tasks.register<JavaExec>("runAccSimulator") {
  group = "application"
  mainClass.set("com.github.prule.acc.client.simulator.AccSimulatorKt")
  classpath = sourceSets["main"].runtimeClasspath
}

tasks.register<JavaExec>("runSampleApp") {
  group = "application"
  mainClass.set("com.github.prule.acc.client.example.AppKt")
  classpath = sourceSets["main"].runtimeClasspath
}
