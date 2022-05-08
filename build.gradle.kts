import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.6.7" apply false
  id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
  id("com.expediagroup.graphql") version "5.3.2" apply false
  id("com.ncorti.ktfmt.gradle") version "0.8.0"

  kotlin("jvm") version "1.6.21"
  kotlin("plugin.spring") version "1.6.21" apply false
  kotlin("plugin.jpa") version "1.6.21" apply false
}

repositories { mavenCentral() }

subprojects {
  group = "com.todo"
  version = "0.0.1-SNAPSHOT"

  repositories { mavenCentral() }

  apply(plugin = "java")
  apply(plugin = "kotlin")
  apply(plugin = "com.ncorti.ktfmt.gradle")

  java.sourceCompatibility = JavaVersion.VERSION_11

  dependencies {
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
      exclude(module = "mockito-core")
    }
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.2.5")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1")
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = "11"
    }
  }

  ktfmt { googleStyle() }

  tasks.withType<Test> {
    useJUnitPlatform()
    reports {
      html.required.set(false)
      junitXml.required.set(true)
    }
  }
}
