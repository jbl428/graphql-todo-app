package com.todo.app.task.service

import com.todo.app.task.repository.TaskQueryRepository
import com.todo.app.task.repository.TaskRepository
import com.todo.lib.entity.task.Task
import com.todo.lib.entity.user.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import javax.persistence.NoResultException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class TaskServiceTest {
  @InjectMockKs private lateinit var taskService: TaskService

  @MockK private lateinit var taskQueryRepository: TaskQueryRepository

  @MockK private lateinit var taskRepository: TaskRepository

  @Nested
  inner class Delete {
    @Test
    fun `존재하지 않는 task 를 제공하면 에러가 발생한다`() {
      // given
      val id = 123L
      val userId = 456L

      every { taskQueryRepository.findOneByUser(id, userId) } throws NoResultException("not found")

      // when
      val result = shouldThrow<NoResultException> { taskService.delete(id, userId) }

      // then
      result.message shouldBe "not found"
    }

    @Test
    fun `주어진 task 를 삭제한다`() {
      // given
      val task =
        Task(
            name = "task",
            completed = false,
            completedAt = null,
            user = User(name = "user", age = 30).also { it.id = 456 },
          )
          .apply { id = 123L }
      val taskId = task.id
      val userId = task.user.id

      every { taskQueryRepository.findOneByUser(task.id, userId) } returns task
      justRun { taskRepository.deleteById(taskId) }

      // when
      taskService.delete(taskId, userId)

      // then
      verify { taskRepository.deleteById(taskId) }
    }
  }
}
