package com.paraooo.local.datasource
import com.paraooo.local.dao.TodoInstanceDao
import com.paraooo.local.entity.TodoInstance
import java.util.UUID

interface TodoInstanceLocalDataSource {

    suspend fun insertTodoInstance(todoInstance: TodoInstance)

    suspend fun insertTodoInstances(instances: List<TodoInstance>)

    suspend fun updateTodoInstance(todoInstance: TodoInstance)

    suspend fun updateTodoProgress(todoInstanceId: UUID, progressAngle: Float)

    suspend fun deleteTodoInstance(todoInstanceId: UUID)

    suspend fun getTodoInstanceById(todoInstanceId: UUID): TodoInstance?

    suspend fun getInstancesByTemplateId(templateId: UUID): List<TodoInstance>

    suspend fun deleteInstancesByDates(templateId: UUID, dates: Set<Long>)

}

