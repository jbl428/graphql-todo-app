package com.todo.app.task.repository

import com.todo.lib.entity.task.Task
import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Long>
