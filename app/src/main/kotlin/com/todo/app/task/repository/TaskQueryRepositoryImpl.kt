package com.todo.app.task.repository

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import com.linecorp.kotlinjdsl.spring.data.singleQuery
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
}
