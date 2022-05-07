package com.todo.app.task.resolver

import com.ninjasquad.springmockk.MockkBean
import com.todo.app.extension.GraphqlBody
import com.todo.app.extension.gqlRequest
import com.todo.app.extension.withSuccess
import com.todo.app.task.repository.dto.TaskWithUserDto
import com.todo.app.task.service.TaskQueryService
import io.mockk.every
import java.time.LocalDateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
internal class TaskQueryResolverTest
@Autowired
constructor(
  private val webTestClient: WebTestClient,
  @MockkBean private val taskQueryService: TaskQueryService,
) {
  @Nested
  inner class Todo {
    @Test
    fun `지정한 todo 를 반환한다`() {
      // given
      val todoId = 1234L
      val query =
        GraphqlBody(
          """query {
            |  todo(todoId: $todoId) {
            |    id 
            |    createdAt
            |    updatedAt
            |    name
            |    completed
            |    completedAt
            |    user {
            |      name
            |      age
            |    }
            |  }
            |}
            """.trimMargin()
        )
      val dto =
        TaskWithUserDto(
          id = todoId,
          createdAt = LocalDateTime.of(2022, 1, 1, 0, 0),
          updatedAt = LocalDateTime.of(2022, 2, 2, 0, 0),
          taskName = "taskName",
          completed = true,
          completedAt = LocalDateTime.of(2022, 3, 3, 0, 0),
          userName = "userName",
          age = 20,
        )

      every { taskQueryService.findOne(dto.id, 1) } returns dto

      // when
      val response = webTestClient.gqlRequest(query)

      // then
      response.withSuccess("todo") {
        expect("id").isEqualTo(dto.id)
        expect("createdAt").isEqualTo("2022-01-01 00:00:00")
        expect("updatedAt").isEqualTo("2022-02-02 00:00:00")
        expect("name").isEqualTo(dto.taskName)
        expect("completed").isEqualTo(dto.completed)
        expect("completedAt").isEqualTo("2022-03-03 00:00:00")
        expect("user.name").isEqualTo(dto.userName)
        expect("user.age").isEqualTo(dto.age)
      }
    }
  }
}
