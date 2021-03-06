plugins {
  id("org.springframework.boot")
  id("io.spring.dependency-management")
  kotlin("jvm")
  kotlin("plugin.spring")
  kotlin("plugin.jpa")
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("com.expediagroup", "graphql-kotlin-spring-server", "5.3.2")
  runtimeOnly("com.h2database:h2")
  runtimeOnly("mysql:mysql-connector-java")
}

allOpen {
  annotation("javax.persistence.Entity")
  annotation("javax.persistence.MappedSuperclass")
  annotation("javax.persistence.Embeddable")
}
