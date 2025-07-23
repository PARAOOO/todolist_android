package com.paraooo.data.repository

import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.data.datasource.TodoInstanceLocalDataSource
import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toModel

class TodoInstanceRepositoryImpl(
    private val dataSource: TodoInstanceLocalDataSource
) : TodoInstanceRepository {
    override suspend fun insertTodoInstance(todoInstance: TodoInstanceModel) {
        dataSource.insertTodoInstance(todoInstance.toDto())
    }

    override suspend fun insertTodoInstances(instances: List<TodoInstanceModel>) {
        dataSource.insertTodoInstances(instances.map { it.toDto() })
    }

    override suspend fun updateTodoInstance(todoInstance: TodoInstanceModel) {
        dataSource.updateTodoInstance(todoInstance.toDto())
    }

    override suspend fun updateTodoProgress(todoInstanceId: Long, progressAngle: Float) {
        dataSource.updateTodoProgress(todoInstanceId, progressAngle)
    }

    override suspend fun deleteTodoInstance(todoInstanceId: Long) {
        dataSource.deleteTodoInstance(todoInstanceId)
    }

    override suspend fun getTodoInstanceById(todoInstanceId: Long): TodoInstanceModel? {
        return dataSource.getTodoInstanceById(todoInstanceId)?.toModel()
    }

    override suspend fun getInstancesByTemplateId(templateId: Long): List<TodoInstanceModel> {
        return dataSource.getInstancesByTemplateId(templateId).map { it.toModel() }
    }

    override suspend fun deleteInstancesByDates(templateId: Long, dates: Set<Long>) {
        dataSource.deleteInstancesByDates(templateId, dates)
    }
}
