package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoModel

interface TodoReadRepository {

    suspend fun getTodoByDate(date : Long) : List<TodoModel>

    suspend fun findTodoById(instanceId : Long) : TodoModel

}