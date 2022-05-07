package com.todo.app.user.repository

import com.todo.lib.entity.user.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>
