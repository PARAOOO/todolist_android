package com.paraooo.domain.repository
import com.paraooo.domain.model.TodoInstanceModel

interface TodoInstanceRepository {

    suspend fun insertTodoInstance(todoInstance: TodoInstanceModel)

    suspend fun getTodoInstanceById(todoInstanceId: Long): TodoInstanceModel?

    suspend fun getInstancesByTemplateId(templateId: Long): List<TodoInstanceModel>

}

