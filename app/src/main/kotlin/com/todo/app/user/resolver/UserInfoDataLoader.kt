package com.todo.app.user.resolver

import com.expediagroup.graphql.server.execution.KotlinDataLoader
import com.todo.app.user.resolver.dto.UserInfo
import com.todo.app.user.service.UserQueryService
import java.util.concurrent.CompletableFuture
import javax.persistence.NoResultException
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory
import org.springframework.stereotype.Component

@Component
class UserInfoDataLoader(
  private val userQueryService: UserQueryService,
) : KotlinDataLoader<Long, UserInfo> {
  companion object {
    const val name = "UserDataLoader"
  }

  override val dataLoaderName = name

  override fun getDataLoader(): DataLoader<Long, UserInfo> =
    DataLoaderFactory.newDataLoader { ids ->
      CompletableFuture.supplyAsync {
        val users = userQueryService.findByIds(ids)

        ids.map { id ->
          val user = users.find { it.id == id } ?: throw NoResultException("사용자가 존재하지 않습니다: $id")

          UserInfo(user.name, user.age)
        }
      }
    }
}
