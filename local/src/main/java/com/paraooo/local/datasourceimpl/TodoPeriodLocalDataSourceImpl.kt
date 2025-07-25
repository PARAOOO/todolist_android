package com.paraooo.local.datasourceimpl

import com.paraooo.local.dao.TodoPeriodDao
import com.paraooo.local.datasource.TodoPeriodLocalDataSource
import com.paraooo.local.entity.TodoPeriod
import com.paraooo.local.entity.TodoPeriodWithTime

internal class TodoPeriodLocalDataSourceImpl(
    private val todoPeriodDao: TodoPeriodDao
) : TodoPeriodLocalDataSource {
    override suspend fun insertTodoPeriod(todoPeriod: TodoPeriod) {
        todoPeriodDao.insertTodoPeriod(todoPeriod)
    }

    override suspend fun updateTodoPeriod(todoPeriod: TodoPeriod) {
        todoPeriodDao.updateTodoPeriod(todoPeriod)
    }

    override suspend fun deleteTodoPeriod(todoPeriod: TodoPeriod) {
        todoPeriodDao.deleteTodoPeriod(todoPeriod)
    }

    override suspend fun getTodoPeriodByTemplateId(templateId: Long): TodoPeriod? {
        return todoPeriodDao.getTodoPeriodByTemplateId(templateId)
    }

    override suspend fun getAlarmPeriodTodos(todayMillis: Long): List<TodoPeriodWithTime> {
        return todoPeriodDao.getAlarmPeriodTodos(todayMillis)
    }
}