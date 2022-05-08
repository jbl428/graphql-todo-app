package com.todo.app.task.service

import com.todo.app.task.repository.TaskQueryRepository
import com.todo.app.task.repository.dto.TaskByUserDto
import com.todo.lib.entity.task.Task
import com.todo.lib.entity.user.User
import io.kotest.matchers.collections.shouldHaveSize
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

  @Nested
  inner class Find {
    @Test
    fun `task 목록을 가져온다`() {
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

      every { taskQueryRepository.findByUser(userId) } returns expectedTasks

      // when
      val result = taskQueryService.find(userId)

      // then
      result shouldHaveSize 2
    }
  }

  @Nested
  inner class FindOne {
    @Test
    fun `task 상세정보를 가져온다`() {
      // given
      val task =
        Task(
          name = "task",
          completed = false,
          completedAt = null,
          user = User("user", 20),
        )
      val userId = 123L

      every { taskQueryRepository.findOneByUser(task.id, userId) } returns task

      // when
      val result = taskQueryService.findOne(task.id, userId)

      // then
      result shouldBe task
    }
  }
}
