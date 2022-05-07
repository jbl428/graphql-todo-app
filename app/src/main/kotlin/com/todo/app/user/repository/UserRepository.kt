package com.todo.app.user.repository

import com.todo.app.user.repository.dto.UserDto
import com.todo.lib.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository : JpaRepository<User, Long> {
  @Query("SELECT u.name as name, u.age as age FROM User u WHERE u.id = :id")
  fun findOne(id: Long): UserDto?
}
