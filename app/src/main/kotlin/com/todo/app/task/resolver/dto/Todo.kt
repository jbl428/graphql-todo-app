package com.todo.app.task.resolver.dto

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.todo.app.task.repository.dto.TaskByUserDto
import com.todo.app.user.resolver.UserInfoDataLoader
import com.todo.app.user.resolver.dto.UserInfo
import com.todo.lib.entity.task.Task
import graphql.schema.DataFetchingEnvironment
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

data class Todo(
  val name: String,
  val completed: Boolean?,
  val id: Int,
  val completedAt: LocalDateTime?,
  val createdAt: LocalDateTime?,
  val updatedAt: LocalDateTime?,
  @GraphQLIgnore val userId: Long,
) {
  fun user(environment: DataFetchingEnvironment): CompletableFuture<UserInfo> =
    environment.getValueFromDataLoader(UserInfoDataLoader.name, userId)

  companion object {
    fun by(task: Task, userId: Long): Todo =
      Todo(
        name = task.name,
        completed = task.completed,
        id = task.id.toInt(),
        completedAt = task.completedAt,
        createdAt = task.createdAt,
        updatedAt = task.updatedAt,
        userId = userId,
      )

    fun by(taskByUserDto: TaskByUserDto, userId: Long): Todo =
      Todo(
        name = taskByUserDto.name,
        completed = taskByUserDto.completed,
        id = taskByUserDto.id.toInt(),
        completedAt = taskByUserDto.completedAt,
        createdAt = taskByUserDto.createdAt,
        updatedAt = taskByUserDto.updatedAt,
        userId = userId,
      )
  }
}
