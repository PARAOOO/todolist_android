package com.paraooo.local.datasource

import com.paraooo.local.dao.TodoPeriodDao
import com.paraooo.local.entity.TodoPeriod
import com.paraooo.local.entity.TodoPeriodWithTime

interface TodoPeriodLocalDataSource {

    suspend fun insertTodoPeriod(todoPeriod: TodoPeriod)

    suspend fun updateTodoPeriod(todoPeriod: TodoPeriod)

    suspend fun deleteTodoPeriod(todoPeriod: TodoPeriod)

    suspend fun getTodoPeriodByTemplateId(templateId: Long): TodoPeriod?

    suspend fun getAlarmPeriodTodos(todayMillis: Long): List<TodoPeriodWithTime>
}