package com.todo.app.task.resolver.dto

import com.todo.app.task.repository.dto.TaskByUserDto
import com.todo.app.task.repository.dto.TaskWithUserDto
import com.todo.app.user.repository.dto.UserDto
import com.todo.app.user.resolver.dto.UserInfo
import com.todo.lib.entity.task.Task
import java.time.LocalDateTime

data class Todo(
  val name: String,
  val completed: Boolean?,
  val id: Int,
  val completedAt: LocalDateTime?,
  val createdAt: LocalDateTime?,
  val updatedAt: LocalDateTime?,
  val user: UserInfo,
) {
  companion object {
    fun by(task: Task): Todo =
      Todo(
        name = task.name,
        completed = task.completed,
        id = task.id.toInt(),
        completedAt = task.completedAt,
        createdAt = task.createdAt,
        updatedAt = task.updatedAt,
        user = UserInfo(task.user.name, task.user.age),
      )

    fun by(dto: TaskWithUserDto): Todo =
      Todo(
        name = dto.taskName,
        completed = dto.completed,
        id = dto.id.toInt(),
        completedAt = dto.completedAt,
        createdAt = dto.createdAt,
        updatedAt = dto.updatedAt,
        user = UserInfo(dto.userName, dto.age),
      )

    fun by(taskByUserDto: TaskByUserDto, userDto: UserDto): Todo =
      Todo(
        name = taskByUserDto.name,
        completed = taskByUserDto.completed,
        id = taskByUserDto.id.toInt(),
        completedAt = taskByUserDto.completedAt,
        createdAt = taskByUserDto.createdAt,
        updatedAt = taskByUserDto.updatedAt,
        user = UserInfo(userDto.name, userDto.age),
      )
  }
}
