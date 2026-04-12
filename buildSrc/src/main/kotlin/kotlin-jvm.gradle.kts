package buildsrc.convention

import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
  kotlin("jvm")
  `maven-publish`
  id("org.jetbrains.dokka")
}

configure<KotlinJvmProjectExtension> { jvmToolchain(21) }

configure<JavaPluginExtension> { withSourcesJar() }

val dokkaJar by
    tasks.registering(Jar::class) {
      archiveClassifier.set("javadoc")
      from(tasks.named("dokkaGeneratePublicationHtml"))
    }

configure<PublishingExtension> {
  publications {
    create<MavenPublication>("maven") {
      from(components["java"])
      artifact(dokkaJar)
    }
  }
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()
  testLogging {
    events(
        TestLogEvent.FAILED,
        TestLogEvent.PASSED,
        TestLogEvent.SKIPPED,
    )
  }
}
