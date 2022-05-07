package com.todo.app.extension

import com.linecorp.kotlinjdsl.query.creator.CriteriaQueryCreatorImpl
import com.linecorp.kotlinjdsl.query.creator.SubqueryCreatorImpl
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactoryImpl
import com.todo.lib.entity.task.Task
import com.todo.lib.entity.user.User
import javax.persistence.EntityManager

fun EntityManager.createQueryFactory() =
  SpringDataQueryFactoryImpl(
    criteriaQueryCreator = CriteriaQueryCreatorImpl(this),
    subqueryCreator = SubqueryCreatorImpl()
  )

fun EntityManager.createUser(user: User = User(name = "user name", age = 30)) =
  user.also(this::persist)

fun EntityManager.createTask(
  task: Task =
    Task(
      name = "task name",
      completed = false,
      completedAt = null,
      user = createUser(),
    )
) = task.also(this::persist)
