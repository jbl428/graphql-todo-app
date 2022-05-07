package com.todo.app

import com.expediagroup.graphql.generator.directives.KotlinDirectiveWiringFactory
import com.todo.app.hook.CustomSchemaGeneratorHooks
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@ConfigurationPropertiesScan
@EntityScan(basePackages = ["com.todo.lib.entity"])
@SpringBootApplication
class TodoApplication {
  @Bean fun wiringFactory() = KotlinDirectiveWiringFactory()

  @Bean
  fun hooks(wiringFactory: KotlinDirectiveWiringFactory) = CustomSchemaGeneratorHooks(wiringFactory)
}

fun main(args: Array<String>) {
  runApplication<TodoApplication>(*args)
}
