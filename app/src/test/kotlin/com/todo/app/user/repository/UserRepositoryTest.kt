package com.todo.app.user.repository

import com.todo.app.extension.createUser
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
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
  inner class FindOne {
    @Test
    fun `존재하지 않는 사용자 요청 시 null 을 반환한다`() {
      // given
      val userId = 100L

      // when
      val result = userRepository.findOne(userId)

      // then
      result.shouldBeNull()
    }

    @Test
    fun `주어진 사용자 정보를 가져온다`() {
      // given
      val user = em.createUser()

      // when
      val result = userRepository.findOne(user.id)

      // then
      result.shouldNotBeNull()
      result.name shouldBe user.name
      result.age shouldBe user.age
    }
  }
}
