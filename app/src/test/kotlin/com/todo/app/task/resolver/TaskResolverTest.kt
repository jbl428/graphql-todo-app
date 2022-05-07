package com.todo.app.task.resolver

import com.ninjasquad.springmockk.MockkBean
import com.todo.app.extension.GraphqlBody
import com.todo.app.extension.gqlRequest
import com.todo.app.extension.withSuccess
import com.todo.app.task.service.TaskService
import com.todo.app.task.service.dto.CreateTaskDto
import com.todo.app.task.service.dto.UpdateTaskDto
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
  inner class CreateTodo {
    @Test
    fun `주어진 todo 를 생성한다`() {
      // given
      val input =
        CreateTaskDto(
          userId = 1,
          name = "new name",
          completed = false,
        )
      val mutation =
        GraphqlBody(
          """mutation {
            |  CreateTodo(todoInput: { name: "${input.name}", completed: ${input.completed} }) {
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
            name = input.name,
            completed = input.completed,
            completedAt = null,
            user = User(name = "user", age = 20).apply { id = input.userId }
          )
          .apply {
            id = 100
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
          }

      every { taskService.create(input, any()) } returns task

      // when
      val response = webTestClient.gqlRequest(mutation)

      // then
      response.withSuccess("CreateTodo") { expect("id").isEqualTo(100) }
    }
  }

  @Nested
  inner class UpdateTodo {
    @Test
    fun `주어진 todo 를 수정한다`() {
      // given
      val input =
        UpdateTaskDto(
          taskId = 100,
          userId = 1,
          name = "new name",
          completed = false,
        )
      val mutation =
        GraphqlBody(
          """mutation {
            |  UpdateTodo(todoId: ${input.taskId}, todoInput: { name: "${input.name}", completed: ${input.completed} }) {
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
            name = input.name,
            completed = input.completed,
            completedAt = null,
            user = User(name = "user", age = 20).apply { id = input.userId }
          )
          .apply {
            id = input.taskId
            createdAt = LocalDateTime.now()
            updatedAt = LocalDateTime.now()
          }

      every { taskService.update(input, any()) } returns task

      // when
      val response = webTestClient.gqlRequest(mutation)

      // then
      response.withSuccess("UpdateTodo") { expect("id").isEqualTo(input.taskId) }
    }
  }

  @Nested
  inner class DeleteTodo {
    @Test
    fun `주어진 todo 를 삭제한다`() {
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
