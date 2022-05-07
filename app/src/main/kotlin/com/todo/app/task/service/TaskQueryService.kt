package com.todo.app.task.service

import com.todo.app.task.repository.TaskQueryRepository
import com.todo.app.task.repository.dto.TaskByUserDto
import com.todo.app.user.repository.UserRepository
import com.todo.lib.entity.user.User
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class TaskQueryService(
  private val taskQueryRepository: TaskQueryRepository,
  private val userRepository: UserRepository,
) {

  fun findAll(userId: Long): Pair<List<TaskByUserDto>, User> {
    val user = userRepository.findByIdOrNull(userId)
    requireNotNull(user) { "사용자가 존재하지 않습니다" }

    val tasks = taskQueryRepository.findByUser(userId)

    return Pair(tasks, user)
  }
}
