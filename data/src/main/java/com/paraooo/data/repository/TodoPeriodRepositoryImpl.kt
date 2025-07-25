package com.paraooo.data.repository

import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoPeriodModel
import com.paraooo.domain.model.TodoPeriodWithTimeModel
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.local.datasource.TodoPeriodLocalDataSource
import com.paraooo.local.entity.TodoPeriod
import com.paraooo.local.entity.TodoPeriodWithTime

internal class TodoPeriodRepositoryImpl(
    private val todoPeriodLocalDataSource: TodoPeriodLocalDataSource
) : TodoPeriodRepository {
    override suspend fun insertTodoPeriod(todoPeriod: TodoPeriodModel) {
        todoPeriodLocalDataSource.insertTodoPeriod(todoPeriod.toEntity())
    }

    override suspend fun updateTodoPeriod(todoPeriod: TodoPeriodModel) {
        todoPeriodLocalDataSource.updateTodoPeriod(todoPeriod.toEntity())
    }

    override suspend fun deleteTodoPeriod(todoPeriod: TodoPeriodModel) {
        todoPeriodLocalDataSource.deleteTodoPeriod(todoPeriod.toEntity())
    }

    override suspend fun getTodoPeriodByTemplateId(templateId: Long): TodoPeriodModel? {
        return todoPeriodLocalDataSource.getTodoPeriodByTemplateId(templateId)?.toModel()
    }

    override suspend fun getAlarmPeriodTodos(todayMillis: Long): List<TodoPeriodWithTimeModel> {
        return todoPeriodLocalDataSource.getAlarmPeriodTodos(todayMillis).map { it.toModel() }
    }
}