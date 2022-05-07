package com.todo.app.user.resolver.dto

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.generator.annotations.GraphQLName

@GraphQLName("User")
data class UserInfo(
  @GraphQLDescription("The user's full name") val name: String,
  val age: Int,
)
