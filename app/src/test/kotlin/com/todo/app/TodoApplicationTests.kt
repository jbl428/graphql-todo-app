package com.todo.app

import io.kotest.matchers.nulls.shouldNotBeNull
import java.io.File
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
internal class TodoApplicationTests(
  @Autowired private val webTestClient: WebTestClient,
) {
  @Test
  fun generateSchema() {
    // when
    val schema =
      webTestClient
        .get()
        .uri("/sdl")
        .exchange()
        .returnResult(String::class.java)
        .responseBody
        .reduce { a, b -> a + "\n" + b }
        .block()

    // then
    schema.shouldNotBeNull()
    File("src/main/resources/graphql/schema.graphql").writeText(schema)
  }
}
