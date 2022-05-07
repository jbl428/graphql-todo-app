package com.todo.app.task.service

import com.todo.app.task.repository.TaskQueryRepository
import com.todo.app.task.repository.TaskRepository
import com.todo.app.task.service.dto.CreateTaskDto
import com.todo.app.task.service.dto.UpdateTaskDto
import com.todo.lib.entity.task.Task
import com.todo.lib.entity.user.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.slot
import io.mockk.verify
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class TaskServiceTest {
  @InjectMockKs private lateinit var taskService: TaskService

  @MockK private lateinit var taskQueryRepository: TaskQueryRepository

  @MockK private lateinit var taskRepository: TaskRepository

  @MockK private lateinit var em: EntityManager

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

  @Nested
  inner class Update {
    @Test
    fun `존재하지 않는 task 를 제공하면 에러가 발생한다`() {
      // given
      val dto = UpdateTaskDto(taskId = 10, userId = 20, name = "task", completed = false)

      every { taskQueryRepository.findOneByUser(dto.taskId, dto.userId) } throws
        NoResultException("not found")

      // when
      val result = shouldThrow<NoResultException> { taskService.update(dto) }

      // then
      result.message shouldBe "not found"
    }

    @Test
    fun `기존 task 의 이름과 완료여부 항목을 갱신한다`() {
      // given
      val task =
        Task(
            name = "task",
            completed = false,
            completedAt = null,
            user = User(name = "user", age = 30).also { it.id = 456 },
          )
          .apply { id = 123L }
      val dto =
        UpdateTaskDto(taskId = task.id, userId = task.user.id, name = "new name", completed = true)

      every { taskQueryRepository.findOneByUser(dto.taskId, dto.userId) } returns task
      every { taskRepository.save(task) } returns task

      // when
      val result = taskService.update(dto)

      // then
      verify { taskRepository.save(task) }

      result.name shouldBe dto.name
      result.completed shouldBe dto.completed
    }

    @Test
    fun `미완료된 task 를 완료로 변경하면 완료시간이 변경된다`() {
      // given
      val task =
        Task(
            name = "task",
            completed = false,
            completedAt = null,
            user = User(name = "user", age = 30).also { it.id = 456 },
          )
          .apply { id = 123L }
      val dto =
        UpdateTaskDto(taskId = task.id, userId = task.user.id, name = "new name", completed = true)
      val expectedCompletedAt = LocalDateTime.now()

      every { taskQueryRepository.findOneByUser(dto.taskId, dto.userId) } returns task
      every { taskRepository.save(task) } returns task

      // when
      val result = taskService.update(dto, expectedCompletedAt)

      // then
      result.completedAt shouldBe expectedCompletedAt
    }

    @Test
    fun `완료된 task 를 미완료로 변경하면 완료시간이 null 이 된다`() {
      // given
      val task =
        Task(
            name = "task",
            completed = true,
            completedAt = LocalDateTime.now(),
            user = User(name = "user", age = 30).also { it.id = 456 },
          )
          .apply { id = 123L }
      val dto =
        UpdateTaskDto(taskId = task.id, userId = task.user.id, name = "new name", completed = false)

      every { taskQueryRepository.findOneByUser(dto.taskId, dto.userId) } returns task
      every { taskRepository.save(task) } returns task

      // when
      val result = taskService.update(dto)

      // then
      result.completedAt.shouldBeNull()
    }
  }

  @Nested
  inner class Create {
    @Test
    fun `새로운 미완료된 task 를 생성한다`() {
      // given
      val dto = CreateTaskDto(userId = 123, name = "new name", completed = false)
      val slot = slot<Task>()

      every { taskRepository.save(capture(slot)) } answers { slot.captured }
      every { em.getReference(User::class.java, dto.userId) } returns
        User(name = "user", age = 30).also { it.id = dto.userId }

      // when
      val result = taskService.create(dto)

      // then
      verify { taskRepository.save(any()) }

      result.name shouldBe dto.name
      result.completed shouldBe dto.completed
      result.user.id shouldBe dto.userId
      result.completedAt.shouldBeNull()
    }

    @Test
    fun `새로운 완료된 task 를 생성한다`() {
      // given
      val dto = CreateTaskDto(userId = 123, name = "new name", completed = true)
      val expectedCompletedAt = LocalDateTime.now()
      val slot = slot<Task>()

      every { taskRepository.save(capture(slot)) } answers { slot.captured }
      every { em.getReference(User::class.java, dto.userId) } returns
        User(name = "user", age = 30).also { it.id = dto.userId }

      // when
      val result = taskService.create(dto, expectedCompletedAt)

      // then
      result.completedAt shouldBe expectedCompletedAt
    }
  }
}
