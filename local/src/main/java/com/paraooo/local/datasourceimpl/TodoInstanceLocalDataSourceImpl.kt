package com.paraooo.local.datasourceimpl
import com.paraooo.local.dao.TodoInstanceDao
import com.paraooo.local.datasource.TodoInstanceLocalDataSource
import com.paraooo.local.entity.TodoInstance
import java.util.UUID

internal class TodoInstanceLocalDataSourceImpl(
    private val todoInstanceDao: TodoInstanceDao
) : TodoInstanceLocalDataSource {
    override suspend fun insertTodoInstance(todoInstance: TodoInstance) {
        todoInstanceDao.insertTodoInstance(todoInstance)
    }

    override suspend fun insertTodoInstances(instances: List<TodoInstance>) {
        todoInstanceDao.insertTodoInstances(instances)
    }

    override suspend fun updateTodoInstance(todoInstance: TodoInstance) {
        todoInstanceDao.updateTodoInstance(todoInstance)
    }

    override suspend fun updateTodoProgress(todoInstanceId: UUID, progressAngle: Float) {
        todoInstanceDao.updateTodoProgress(todoInstanceId, progressAngle)
    }

    override suspend fun deleteTodoInstance(todoInstanceId: UUID) {
        todoInstanceDao.deleteTodoInstance(todoInstanceId)
    }

    override suspend fun getTodoInstanceById(todoInstanceId: UUID): TodoInstance? {
        return todoInstanceDao.getTodoInstanceById(todoInstanceId)
    }

    override suspend fun getInstancesByTemplateId(templateId: UUID): List<TodoInstance> {
        return todoInstanceDao.getInstancesByTemplateId(templateId)
    }

    override suspend fun deleteInstancesByDates(templateId: UUID, dates: Set<Long>) {
        todoInstanceDao.deleteInstancesByDates(templateId, dates)
    }
}

