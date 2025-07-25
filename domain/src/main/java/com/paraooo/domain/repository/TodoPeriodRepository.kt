package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoPeriodModel
import com.paraooo.domain.model.TodoPeriodWithTimeModel

interface TodoPeriodRepository {

    suspend fun insertTodoPeriod(todoPeriod: TodoPeriodModel)

    suspend fun updateTodoPeriod(todoPeriod: TodoPeriodModel)

    suspend fun deleteTodoPeriod(todoPeriod: TodoPeriodModel)

    suspend fun getTodoPeriodByTemplateId(templateId: Long): TodoPeriodModel?

    suspend fun getAlarmPeriodTodos(todayMillis: Long): List<TodoPeriodWithTimeModel>
}