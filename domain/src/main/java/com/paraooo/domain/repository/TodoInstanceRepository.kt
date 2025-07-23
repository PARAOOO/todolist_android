package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoInstanceModel

interface TodoInstanceRepository {
    suspend fun insertTodoInstance(todoInstance: TodoInstanceModel)
    suspend fun insertTodoInstances(instances: List<TodoInstanceModel>)
    suspend fun updateTodoInstance(todoInstance: TodoInstanceModel)
    suspend fun updateTodoProgress(todoInstanceId: Long, progressAngle: Float)
    suspend fun deleteTodoInstance(todoInstanceId: Long)
    suspend fun getTodoInstanceById(todoInstanceId: Long): TodoInstanceModel?
    suspend fun getInstancesByTemplateId(templateId: Long): List<TodoInstanceModel>
    suspend fun deleteInstancesByDates(templateId: Long, dates: Set<Long>)
}
