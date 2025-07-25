package com.paraooo.local.datasource
import com.paraooo.local.dao.TodoInstanceDao
import com.paraooo.local.entity.TodoInstance

interface TodoInstanceLocalDataSource {

    suspend fun insertTodoInstance(todoInstance: TodoInstance)

    suspend fun insertTodoInstances(instances: List<TodoInstance>)

    suspend fun updateTodoInstance(todoInstance: TodoInstance)

    suspend fun updateTodoProgress(todoInstanceId: Long, progressAngle: Float)

    suspend fun deleteTodoInstance(todoInstanceId: Long)

    suspend fun getTodoInstanceById(todoInstanceId: Long): TodoInstance?

    suspend fun getInstancesByTemplateId(templateId: Long): List<TodoInstance>

    suspend fun deleteInstancesByDates(templateId: Long, dates: Set<Long>)

}

