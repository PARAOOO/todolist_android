package com.paraooo.domain.repository
import com.paraooo.domain.model.TodoInstanceModel
import java.util.UUID

interface TodoInstanceRepository {

    suspend fun insertTodoInstance(todoInstance: TodoInstanceModel)

    suspend fun getTodoInstanceById(todoInstanceId: UUID): TodoInstanceModel?

    suspend fun getInstancesByTemplateId(templateId: UUID): List<TodoInstanceModel>

}

