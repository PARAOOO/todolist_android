package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import java.time.LocalDate

interface TodoRepository {

    suspend fun getTodoInstanceByInstanceId(instanceId: Long) : TodoInstanceModel?

    suspend fun postTodo(todoTemplate: TodoTemplateModel, todoInstance : TodoInstanceModel) : Long

    suspend fun updateTodo(todoTemplate: TodoTemplateModel, todoInstance: TodoInstanceModel)
}