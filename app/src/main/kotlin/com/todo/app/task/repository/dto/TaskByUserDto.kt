package com.todo.app.task.repository.dto

import java.time.LocalDateTime

data class TaskByUserDto(
  val id: Long,
  val createdAt: LocalDateTime,
  val updatedAt: LocalDateTime,
  val name: String,
  val completed: Boolean,
  val completedAt: LocalDateTime?,
)
