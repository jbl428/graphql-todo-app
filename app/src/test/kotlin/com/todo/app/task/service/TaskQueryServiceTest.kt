package com.todo.app.task.service

import com.todo.app.task.repository.TaskQueryRepository
import com.todo.app.task.repository.dto.TaskByUserDto
import com.todo.app.user.repository.UserRepository
import com.todo.lib.entity.user.User
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
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(MockKExtension::class)
internal class TaskQueryServiceTest {
  @InjectMockKs private lateinit var taskQueryService: TaskQueryService

  @MockK private lateinit var taskQueryRepository: TaskQueryRepository

  @MockK private lateinit var userRepository: UserRepository

  @Nested
  inner class FindAll {
    @Test
    fun `존재하지 않는 사용자로 요청 시 에러가 발생한다`() {
      // given
      val userId = 123L

      every { userRepository.findByIdOrNull(userId) } returns null

      // when
      val result = shouldThrow<IllegalArgumentException> { taskQueryService.findAll(userId) }

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
      val expectedUser = User(name = "user1", age = 10)

      every { taskQueryRepository.findByUser(userId) } returns expectedTasks
      every { userRepository.findByIdOrNull(userId) } returns expectedUser

      // when
      val (tasks, user) = taskQueryService.findAll(userId)

      // then
      tasks shouldBe expectedTasks
      user shouldBe expectedUser
    }
  }
}
