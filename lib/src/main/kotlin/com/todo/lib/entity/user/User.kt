package com.todo.lib.entity.user

import com.todo.lib.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class User(
  @Column(nullable = false) var name: String,
  @Column(nullable = false) var age: Int,
) : BaseEntity()
