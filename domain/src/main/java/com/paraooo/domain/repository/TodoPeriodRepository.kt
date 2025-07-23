package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoPeriodModel

interface TodoPeriodRepository {
    suspend fun insertTodoPeriod(todoPeriod: TodoPeriodModel)
    suspend fun updateTodoPeriod(todoPeriod: TodoPeriodModel)
    suspend fun getTodoPeriodByTemplateId(templateId: Long): TodoPeriodModel?
}
