package com.todo.app.task.resolver

import com.ninjasquad.springmockk.MockkBean
import com.todo.app.extension.GraphqlBody
import com.todo.app.extension.gqlRequest
import com.todo.app.extension.withSuccess
import com.todo.app.task.service.TaskService
import com.todo.lib.entity.task.Task
import com.todo.lib.entity.user.User
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
internal class TaskResolverTest
@Autowired
constructor(
  private val webTestClient: WebTestClient,
  @MockkBean private val taskService: TaskService,
) {
  @Nested
  inner class DeleteTodo {
    @Test
    fun `지정한 todo 삭제한다`() {
      // given
      val todoId = 1234L
      val mutation =
        GraphqlBody(
          """mutation {
            |  DeleteTodo(todoId: $todoId) {
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
      val task =
        Task(
            name = "task",
            completed = false,
            completedAt = null,
            user = User(name = "user", age = 20).apply { id = 1 }
          )
          .apply {
            id = todoId
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
          }

      every { taskService.delete(todoId, 1) } returns task

      // when
      val response = webTestClient.gqlRequest(mutation)

      // then
      response.withSuccess("DeleteTodo") {
        expect("id").isEqualTo(todoId)
        expect("name").isEqualTo(task.name)
        expect("completed").isEqualTo(task.completed)
        expect("completedAt").isEmpty
      }
    }
  }
}
