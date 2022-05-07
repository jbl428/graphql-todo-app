package com.todo.app.task.resolver.dto

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
    fun byTask(task: Task): Todo =
      Todo(
        name = task.name,
        completed = task.completed,
        id = task.id.toInt(),
        completedAt = task.completedAt,
        createdAt = task.createdAt,
        updatedAt = task.updatedAt,
        user = UserInfo(task.user.name, task.user.age),
      )
  }
}
