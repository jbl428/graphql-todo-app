package com.todo.app.user

import com.todo.app.user.repository.UserRepository
import com.todo.lib.entity.user.User
import javax.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class UserPostConstructor(
  private val userRepository: UserRepository,
) {
  private val userId = 1L

  @PostConstruct
  fun postConstruct() {
    userRepository.findById(userId).orElseGet {
      val newUser = User(name = "user name", age = 30).apply { id = userId }
      userRepository.save(newUser)
    }
  }
}
