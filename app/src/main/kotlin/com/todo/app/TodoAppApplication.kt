package com.todo.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@ConfigurationPropertiesScan
@EntityScan(basePackages = ["com.todo.lib.entity"])
@SpringBootApplication
class AppApplication

fun main(args: Array<String>) {
  runApplication<AppApplication>(*args)
}
