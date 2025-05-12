package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoModel
import kotlinx.coroutines.flow.Flow

interface TodoReadRepository {

    suspend fun getTodoByDate(date : Long) : Flow<List<TodoModel>>

    suspend fun findTodoById(instanceId : Long) : TodoModel

}