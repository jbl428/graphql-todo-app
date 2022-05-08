package com.todo.app.task.resolver

import com.ninjasquad.springmockk.MockkBean
import com.todo.app.extension.GraphqlBody
import com.todo.app.extension.gqlRequest
import com.todo.app.extension.withSuccess
import com.todo.app.task.repository.dto.TaskByUserDto
import com.todo.app.task.service.TaskQueryService
import com.todo.app.user.repository.dto.UserDto
import com.todo.app.user.service.UserQueryService
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
internal class TaskQueryResolverTest
@Autowired
constructor(
  private val webTestClient: WebTestClient,
  @MockkBean private val taskQueryService: TaskQueryService,
  @MockkBean private val userQueryService: UserQueryService,
) {
  @Nested
  inner class Todos {
    @Test
    fun `todo 목록를 반환한다`() {
      // given
      val todoId = 1234L
      val query =
        GraphqlBody(
          """query {
            |  todos {
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
      val taskDto =
        TaskByUserDto(
          id = todoId,
          createdAt = LocalDateTime.of(2022, 1, 1, 0, 0),
          updatedAt = LocalDateTime.of(2022, 2, 2, 0, 0),
          name = "taskName",
          completed = true,
          completedAt = LocalDateTime.of(2022, 3, 3, 0, 0),
        )
      val userDto =
        object : UserDto {
          override val id: Long = 1
          override val name: String = "userName"
          override val age: Int = 20
        }

      every { taskQueryService.find(1) } returns listOf(taskDto)
      every { userQueryService.findByIds(listOf(userDto.id)) } returns listOf(userDto)

      // when
      val response = webTestClient.gqlRequest(query)

      // then
      response.withSuccess("todos") {
        expect("[0].id").isEqualTo(taskDto.id)
        expect("[0].createdAt").isEqualTo("2022-01-01 00:00:00")
        expect("[0].updatedAt").isEqualTo("2022-02-02 00:00:00")
        expect("[0].name").isEqualTo(taskDto.name)
        expect("[0].completed").isEqualTo(taskDto.completed)
        expect("[0].completedAt").isEqualTo("2022-03-03 00:00:00")
        expect("[0].user.name").isEqualTo(userDto.name)
        expect("[0].user.age").isEqualTo(userDto.age)
      }
    }
  }

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
      val task =
        Task(
            name = "name",
            completed = true,
            completedAt = LocalDateTime.of(2022, 3, 3, 0, 0),
            user = User(name = "userName", age = 20),
          )
          .apply {
            id = todoId
            createdAt = LocalDateTime.of(2022, 1, 1, 0, 0)
            updatedAt = LocalDateTime.of(2022, 2, 2, 0, 0)
          }
      val userDto =
        object : UserDto {
          override val id: Long = 1
          override val name: String = "userName"
          override val age: Int = 20
        }

      every { taskQueryService.findOne(task.id, 1) } returns task
      every { userQueryService.findByIds(listOf(userDto.id)) } returns listOf(userDto)

      // when
      val response = webTestClient.gqlRequest(query)

      // then
      response.withSuccess("todo") {
        expect("id").isEqualTo(task.id)
        expect("createdAt").isEqualTo("2022-01-01 00:00:00")
        expect("updatedAt").isEqualTo("2022-02-02 00:00:00")
        expect("name").isEqualTo(task.name)
        expect("completed").isEqualTo(task.completed)
        expect("completedAt").isEqualTo("2022-03-03 00:00:00")
        expect("user.name").isEqualTo(userDto.name)
        expect("user.age").isEqualTo(userDto.age)
      }
    }
  }
}
