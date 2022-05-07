package com.todo.app.task.repository.dto

import java.time.LocalDateTime

data class TaskWithUserDto(
  val id: Long,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
  val taskName: String,
  val completed: Boolean,
  val completedAt: LocalDateTime?,
  val userName: String,
  val age: Int,
)
