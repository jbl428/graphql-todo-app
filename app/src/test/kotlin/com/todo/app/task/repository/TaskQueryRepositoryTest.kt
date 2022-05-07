package com.todo.app.task.repository

import com.todo.app.extension.createQueryFactory
import com.todo.app.extension.createTask
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import javax.persistence.EntityManager
import javax.persistence.NoResultException
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class TaskQueryRepositoryTest
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
}
