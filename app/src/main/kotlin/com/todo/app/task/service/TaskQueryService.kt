package com.todo.app.task.service

import com.todo.app.task.repository.TaskQueryRepository
import com.todo.app.task.repository.dto.TaskByUserDto
import com.todo.lib.entity.task.Task
import org.springframework.stereotype.Service

@Service
class TaskQueryService(
  private val taskQueryRepository: TaskQueryRepository,
) {

  fun find(userId: Long): List<TaskByUserDto> = taskQueryRepository.findByUser(userId)

  fun findOne(id: Long, userId: Long): Task = taskQueryRepository.findOneByUser(id, userId)
}
