# graphql-todo-app

![build](https://github.com/jbl428/graphql-todo-app/actions/workflows/gradle.yml/badge.svg)

간단한 graphql todo api

## 실행 방법

> 요구사항: docker

프로젝트 루트에서 아래 명령어를 실행해 mysql 을 실행합니다.

```shell
docker-compose up -d
```

이후 아래 명령어를 실행합니다.

```shell
./gradlew :app:bootRun   
```

실행 시 user 테이블에 데이터가 없다면 유저를 하나 생성합니다.

## 스키마

[TodoApplicationTest](./app/src/test/kotlin/com/todo/app/TodoApplicationTests.kt) 테스트를 실행하면 스키마가 업데이트 됩니다.

다음 경로에 스키마 파일이 존재합니다. [schema.graphql](./app/src/main/resources/graphql/schema.graphql)

## Playground

서버 실행 후 [http://localhost:8080/playground](http://localhost:8080/playground) 에서 `graphql playground` 에 접속할 수 있습니다.

## 샘플 쿼리

```graphql
mutation create {
    CreateTodo(todoInput: { name: "task 1", completed: false }) {
        id
        name
        completed
        completedAt
        user {
            name
            age
        }
    }
}

mutation update {
    UpdateTodo(todoId: 1, todoInput: { name: "new task", completed: true }) {
        id
        name
        completed
        completedAt
        user {
            name
            age
        }
    }
}

mutation delete {
    DeleteTodo(todoId: 1) {
        id
        name
        completed
        completedAt
        user {
            name
            age
        }
    }
}

query todos {
    todos {
        id
        name
        completed
        completedAt
        user {
            name
            age
        }
    }
}

query todo {
    todo(todoId: 1) {
        id
        name
        completed
        completedAt
        user {
            name
            age
        }
    }
}
```
