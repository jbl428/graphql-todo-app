package com.todo.app.task.resolver.dto

import com.todo.app.task.service.dto.CreateTaskDto
import com.todo.app.task.service.dto.UpdateTaskDto

data class TodoInput(
  val name: String,
  val completed: Boolean,
) {
  fun toCreateDto(userId: Long) = CreateTaskDto(userId, name, completed)

  fun toUpdateDto(taskId: Long, userId: Long) = UpdateTaskDto(taskId, userId, name, completed)
}
