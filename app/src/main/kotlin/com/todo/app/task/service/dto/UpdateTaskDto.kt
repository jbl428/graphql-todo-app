package com.todo.app.task.service.dto

data class UpdateTaskDto(
  val taskId: Long,
  val userId: Long,
  val name: String,
  val completed: Boolean,
)
