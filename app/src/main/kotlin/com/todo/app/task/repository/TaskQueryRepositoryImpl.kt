package com.todo.app.task.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.listQuery
import com.linecorp.kotlinjdsl.spring.data.singleQuery
import com.todo.app.task.repository.dto.TaskByUserDto
import com.todo.app.task.repository.dto.TaskWithUserDto
import com.todo.lib.entity.task.Task
import com.todo.lib.entity.user.User
import org.springframework.stereotype.Repository

@Repository
class TaskQueryRepositoryImpl(
  private val springDataQueryFactory: SpringDataQueryFactory,
) : TaskQueryRepository {

  override fun findOneByUser(id: Long, userId: Long): Task =
    springDataQueryFactory.singleQuery {
      select(entity(Task::class))
      from(Task::class)
      join(Task::user)
      where(
        and(
          col(Task::id).equal(id),
          col(User::id).equal(userId),
        )
      )
    }

  override fun findByUser(userId: Long): List<TaskByUserDto> =
    springDataQueryFactory.listQuery {
      selectMulti(
        col(Task::id),
        col(Task::createdAt),
        col(Task::updatedAt),
        col(Task::name),
        col(Task::completed),
        col(Task::completedAt),
      )
      from(Task::class)
      join(Task::user)
      where(col(User::id).equal(userId))
      orderBy(col(Task::id).desc())
    }

  override fun findOneWithUser(id: Long, userId: Long): TaskWithUserDto =
    springDataQueryFactory.singleQuery {
      selectMulti(
        col(Task::id),
        col(Task::createdAt),
        col(Task::updatedAt),
        col(Task::name),
        col(Task::completed),
        col(Task::completedAt),
        col(User::name),
        col(User::age),
      )
      from(Task::class)
      join(Task::user)
      where(
        and(
          col(Task::id).equal(id),
          col(User::id).equal(userId),
        )
      )
    }
}
