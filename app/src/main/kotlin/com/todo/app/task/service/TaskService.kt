package com.todo.app.task.service

import com.todo.app.task.repository.TaskQueryRepository
import com.todo.app.task.repository.TaskRepository
import com.todo.app.task.service.dto.UpdateTaskDto
import com.todo.lib.entity.task.Task
import java.time.LocalDateTime

class TaskService(
  private val taskQueryRepository: TaskQueryRepository,
  private val taskRepository: TaskRepository,
) {

  fun delete(id: Long, userId: Long) {
    val task = taskQueryRepository.findOneByUser(id, userId)

    taskRepository.deleteById(task.id)
  }

  fun update(dto: UpdateTaskDto, now: LocalDateTime = LocalDateTime.now()): Task {
    val task = taskQueryRepository.findOneByUser(dto.taskId, dto.userId)

    val completedAt = if (dto.completed) now else null

    task.update(dto.name, dto.completed, completedAt)

    return taskRepository.save(task)
  }
}
