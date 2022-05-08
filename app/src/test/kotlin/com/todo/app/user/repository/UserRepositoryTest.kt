package com.todo.app.user.repository

import com.todo.app.extension.createUser
import com.todo.lib.entity.user.User
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import javax.persistence.EntityManager
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
internal class UserRepositoryTest
@Autowired
constructor(
  private val userRepository: UserRepository,
  private val em: EntityManager,
) {

  @Nested
  inner class FindByIds {
    @Test
    fun `존재하지 않는 사용자 요청 시 빈리스트를 반환한다`() {
      // given
      val userId = 100L

      // when
      val result = userRepository.findByIds(listOf(userId))

      // then
      result.shouldBeEmpty()
    }

    @Test
    fun `주어진 사용자 정보를 가져온다`() {
      // given
      val user1 = em.createUser(User("user1", 20))
      val user2 = em.createUser(User("user2", 30))

      // when
      val result = userRepository.findByIds(listOf(user1.id, user2.id))

      // then
      result shouldHaveSize 2
      result.map { it.id } shouldBe listOf(user1.id, user2.id)
    }
  }
}
