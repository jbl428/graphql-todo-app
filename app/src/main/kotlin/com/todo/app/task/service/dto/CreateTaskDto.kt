package com.todo.app.task.service.dto

data class CreateTaskDto(
  val userId: Long,
  val name: String,
  val completed: Boolean,
)
