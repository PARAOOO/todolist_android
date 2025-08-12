package com.paraooo.data.repository
import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.local.datasource.TodoInstanceLocalDataSource

internal class TodoInstanceRepositoryImpl(
    private val todoInstanceLocalDataSource: TodoInstanceLocalDataSource
) : TodoInstanceRepository {
    override suspend fun insertTodoInstance(todoInstance: TodoInstanceModel) {
        todoInstanceLocalDataSource.insertTodoInstance(todoInstance.toEntity())
    }

    override suspend fun getTodoInstanceById(todoInstanceId: Long): TodoInstanceModel? {
        return todoInstanceLocalDataSource.getTodoInstanceById(todoInstanceId)?.toModel()
    }

    override suspend fun getInstancesByTemplateId(templateId: Long): List<TodoInstanceModel> {
        return todoInstanceLocalDataSource.getInstancesByTemplateId(templateId).map { it.toModel() }
    }
}

