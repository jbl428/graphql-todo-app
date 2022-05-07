package com.todo.app.task.service

import com.todo.app.task.repository.TaskQueryRepository
import com.todo.app.task.repository.dto.TaskByUserDto
import com.todo.app.task.repository.dto.TaskWithUserDto
import com.todo.app.user.repository.UserRepository
import com.todo.app.user.repository.dto.UserDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import java.time.LocalDateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class TaskQueryServiceTest {
  @InjectMockKs private lateinit var taskQueryService: TaskQueryService

  @MockK private lateinit var taskQueryRepository: TaskQueryRepository

  @MockK private lateinit var userRepository: UserRepository

  @Nested
  inner class Find {
    @Test
    fun `존재하지 않는 사용자로 요청 시 에러가 발생한다`() {
      // given
      val userId = 123L

      every { userRepository.findOne(userId) } returns null

      // when
      val result = shouldThrow<IllegalArgumentException> { taskQueryService.find(userId) }

      // then
      result.message shouldBe "사용자가 존재하지 않습니다"
    }

    @Test
    fun `사용자가 소유한 task 목록과 사용자 정보를 가져온다`() {
      // given
      val userId = 123L
      val expectedTasks =
        listOf(
          TaskByUserDto(
            id = 1,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            name = "task1",
            completed = false,
            completedAt = null,
          ),
          TaskByUserDto(
            id = 2,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            name = "task2",
            completed = false,
            completedAt = null,
          ),
        )
      val expectedUser =
        object : UserDto {
          override val name: String = "user"
          override val age: Int = 20
        }

      every { userRepository.findOne(userId) } returns expectedUser
      every { taskQueryRepository.findByUser(userId) } returns expectedTasks

      // when
      val (tasks, user) = taskQueryService.find(userId)

      // then
      tasks shouldBe expectedTasks
      user shouldBe expectedUser
    }
  }

  @Nested
  inner class FindOne {
    @Test
    fun `task 와 사용자 정보를 가져온다`() {
      // given
      val dto =
        TaskWithUserDto(
          id = 10,
          createdAt = LocalDateTime.now(),
          updatedAt = LocalDateTime.now(),
          taskName = "task",
          completed = false,
          completedAt = null,
          userName = "user",
          age = 20,
        )
      val userId = 123L

      every { taskQueryRepository.findOneWithUser(dto.id, userId) } returns dto

      // when
      val result = taskQueryService.findOne(dto.id, userId)

      // then
      result shouldBe dto
    }
  }
}
