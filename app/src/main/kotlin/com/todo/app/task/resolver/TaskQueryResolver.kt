package com.todo.app.task.resolver

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.operations.Query
import com.todo.app.task.resolver.dto.Todo
import com.todo.app.task.service.TaskQueryService
import org.springframework.stereotype.Component

@Component
class TaskQueryResolver(
  private val taskQueryService: TaskQueryService,
) : Query {
  // TODO: 인증 구현 후 세션에서 가져오기
  private val userId = 1L

  @GraphQLName("todos")
  fun find(): List<Todo> =
    taskQueryService.find(userId).let { (tasks, user) -> tasks.map { Todo.by(it, user) } }

  @GraphQLName("todo")
  fun findOne(todoId: Int): Todo? = taskQueryService.findOne(todoId.toLong(), userId).let(Todo::by)
}
