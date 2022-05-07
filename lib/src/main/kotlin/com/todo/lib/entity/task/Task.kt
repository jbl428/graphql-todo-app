package com.todo.lib.entity.task

import com.todo.lib.entity.BaseEntity
import com.todo.lib.entity.user.User
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
class Task(
  @Column(nullable = false) var name: String,
  @Column(nullable = false) var completed: Boolean,
  @Column var completedAt: LocalDateTime?,
  @ManyToOne(fetch = FetchType.LAZY) @field:NotNull var user: User,
) : BaseEntity() {

  fun update(name: String, completed: Boolean, completedAt: LocalDateTime?) {
    this.name = name
    this.completed = completed
    this.completedAt = completedAt
  }
}
