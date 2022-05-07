package com.todo.app.task.service

import com.todo.app.task.repository.TaskQueryRepository
import com.todo.app.task.repository.TaskRepository

class TaskService(
  private val taskQueryRepository: TaskQueryRepository,
  private val taskRepository: TaskRepository,
) {

  fun delete(id: Long, userId: Long) {
    val task = taskQueryRepository.findOneByUser(id, userId)

    taskRepository.deleteById(task.id)
  }
}
