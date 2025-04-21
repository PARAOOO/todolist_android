package com.paraooo.data.datasource

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.paraooo.data.dto.TodoInstanceDto
import com.paraooo.data.local.dao.TodoInstanceDao
import com.paraooo.data.local.entity.TodoInstance
import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toEntity

class TodoInstanceLocalDataSource(
    private val todoInstanceDao: TodoInstanceDao
) {
    suspend fun insertTodoInstance(todoInstance: TodoInstanceDto) {
        todoInstanceDao.insertTodoInstance(todoInstance.toEntity())
    }

    suspend fun insertTodoInstances(instances: List<TodoInstanceDto>) {
        todoInstanceDao.insertTodoInstances(instances.map { it.toEntity() })
    }

    suspend fun updateTodoInstance(todoInstance: TodoInstanceDto) {
        todoInstanceDao.updateTodoInstance(todoInstance.toEntity())
    }

    suspend fun updateTodoProgress(todoInstanceId: Long, progressAngle: Float) {
        todoInstanceDao.updateTodoProgress(todoInstanceId, progressAngle)
    }

    suspend fun deleteTodoInstance(todoInstanceId: Long) {
        todoInstanceDao.deleteTodoInstance(todoInstanceId)
    }

    suspend fun getTodoInstanceById(todoInstanceId: Long): TodoInstanceDto? {
        return todoInstanceDao.getTodoInstanceById(todoInstanceId)?.toDto()
    }

    suspend fun getInstancesByTemplateId(templateId: Long): List<TodoInstanceDto> {
        return todoInstanceDao.getInstancesByTemplateId(templateId).map { it.toDto() }
    }

    suspend fun deleteInstancesByDates(templateId: Long, dates: Set<Long>) {
        todoInstanceDao.deleteInstancesByDates(templateId, dates)
    }
}

