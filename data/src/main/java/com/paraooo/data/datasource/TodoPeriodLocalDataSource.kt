package com.paraooo.data.datasource

import com.paraooo.data.dto.TodoPeriodDto
import com.paraooo.data.dto.TodoPeriodWithTimeDto
import com.paraooo.data.local.dao.TodoPeriodDao
import com.paraooo.data.local.entity.TodoPeriodWithTime
import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toEntity

class TodoPeriodLocalDataSource(
    private val todoPeriodDao: TodoPeriodDao
) {
    suspend fun insertTodoPeriod(todoPeriod: TodoPeriodDto) {
        todoPeriodDao.insertTodoPeriod(todoPeriod.toEntity())
    }

    suspend fun updateTodoPeriod(todoPeriod: TodoPeriodDto) {
        todoPeriodDao.updateTodoPeriod(todoPeriod.toEntity())
    }

    suspend fun deleteTodoPeriod(todoPeriod: TodoPeriodDto) {
        todoPeriodDao.deleteTodoPeriod(todoPeriod.toEntity())
    }

    suspend fun getTodoPeriodByTemplateId(templateId: Long): TodoPeriodDto? {
        return todoPeriodDao.getTodoPeriodByTemplateId(templateId)?.toDto()
    }

    suspend fun getAlarmPeriodTodos(todayMillis: Long): List<TodoPeriodWithTimeDto> {
        return todoPeriodDao.getAlarmPeriodTodos(todayMillis).map { it.toDto() }
    }
}