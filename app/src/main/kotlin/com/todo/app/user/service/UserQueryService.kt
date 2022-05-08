package com.todo.app.user.service

import com.todo.app.user.repository.UserRepository
import com.todo.app.user.repository.dto.UserDto
import org.springframework.stereotype.Service

@Service
class UserQueryService(
  private val userRepository: UserRepository,
) {

  fun findByIds(ids: List<Long>): List<UserDto> = userRepository.findByIds(ids)
}
