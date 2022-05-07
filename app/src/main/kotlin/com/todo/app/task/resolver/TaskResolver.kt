package com.todo.app.task.resolver

import com.expediagroup.graphql.generator.annotations.GraphQLName
import com.expediagroup.graphql.server.operations.Mutation
import com.todo.app.task.resolver.dto.Todo
import com.todo.app.task.resolver.dto.TodoInput
import com.todo.app.task.service.TaskService
import org.springframework.stereotype.Component

@Component
class TaskResolver(
  private val taskService: TaskService,
) : Mutation {
  // TODO: 인증 구현 후 세션으로 부터 가져오기
  private val userId = 1L

  @GraphQLName("UpdateTodo")
  fun update(todoId: Int, input: TodoInput): Todo? =
    taskService.update(input.toUpdateDto(todoId.toLong(), userId)).let(Todo::by)

  @GraphQLName("DeleteTodo")
  fun delete(todoId: Int): Todo? = taskService.delete(todoId.toLong(), userId).let(Todo::by)
}
