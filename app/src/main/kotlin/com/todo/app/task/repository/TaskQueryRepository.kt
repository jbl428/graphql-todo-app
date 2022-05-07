package com.todo.app.task.repository

import com.todo.app.task.repository.dto.TaskByUserDto
import com.todo.lib.entity.task.Task

interface TaskQueryRepository {
  fun findOneByUser(id: Long, userId: Long): Task
  fun findByUser(userId: Long): List<TaskByUserDto>
}
