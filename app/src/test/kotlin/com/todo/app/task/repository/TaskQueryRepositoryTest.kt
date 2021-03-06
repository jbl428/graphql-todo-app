package com.todo.app.task.repository

import com.todo.app.extension.createQueryFactory
import com.todo.app.extension.createTask
import com.todo.app.extension.createUser
import com.todo.lib.entity.task.Task
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class TaskQueryRepositoryTest
@Autowired
constructor(
  private val em: EntityManager,
) {
  private val appUserAppRepository = TaskQueryRepositoryImpl(em.createQueryFactory())

  @Nested
  inner class FindOneByUser {
    @Test
    fun `user 가 소유하지 않은 task 를 제공하면 에러가 발생한다`() {
      // given
      val task = em.createTask()
      val invalidTaskId = task.id + 1

      // when
      val result =
        shouldThrow<NoResultException> {
          appUserAppRepository.findOneByUser(invalidTaskId, task.user.id)
        }

      // then
      result.message shouldBe "No entity found for query"
    }

    @Test
    fun `task 를 소유하지 않은 user 를 제공하면 에러가 발생한다`() {
      // given
      val task = em.createTask()
      val otherUserId = task.user.id + 1

      // when
      val result =
        shouldThrow<NoResultException> { appUserAppRepository.findOneByUser(task.id, otherUserId) }

      // then
      result.message shouldBe "No entity found for query"
    }

    @Test
    fun `주어진 user 가 소유한 task 를 가져온다`() {
      // given
      val task = em.createTask()

      // when
      val result = appUserAppRepository.findOneByUser(task.id, task.user.id)

      // then
      result shouldBe task
    }
  }

  @Nested
  inner class FindByUser {
    @Test
    fun `user 가 소유한 task 가 없으면 빈리스트를 반환한다`() {
      // given
      val user = em.createUser()

      // when
      val result = appUserAppRepository.findByUser(user.id)

      // then
      result.shouldBeEmpty()
    }

    @Test
    fun `user 가 소유한 task 를 최신순으로 반환한다`() {
      // given
      val user = em.createUser()
      val expectedSize = 3
      val now = LocalDateTime.now()
      repeat(expectedSize) {
        em.createTask(Task(name = "task1", completed = true, completedAt = now, user = user))
      }

      val otherUser = em.createUser()
      em.createTask(Task(name = "task2", completed = false, completedAt = null, user = otherUser))

      // when
      val result = appUserAppRepository.findByUser(user.id)

      // then
      result shouldHaveSize expectedSize
      result.forEach {
        it.name shouldBe "task1"
        it.completed.shouldBeTrue()
        it.completedAt shouldBe now
      }

      val taskIds = result.map { it.id }
      taskIds shouldBe taskIds.sortedDescending()
    }
  }
}
