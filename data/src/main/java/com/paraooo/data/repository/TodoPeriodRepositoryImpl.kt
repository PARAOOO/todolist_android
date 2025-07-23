package com.paraooo.data.repository

import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.model.TodoPeriodModel
import com.paraooo.data.datasource.TodoPeriodLocalDataSource
import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toModel

class TodoPeriodRepositoryImpl(
    private val dataSource: TodoPeriodLocalDataSource
) : TodoPeriodRepository {
    override suspend fun insertTodoPeriod(todoPeriod: TodoPeriodModel) {
        dataSource.insertTodoPeriod(todoPeriod.toDto())
    }

    override suspend fun updateTodoPeriod(todoPeriod: TodoPeriodModel) {
        dataSource.updateTodoPeriod(todoPeriod.toDto())
    }

    override suspend fun getTodoPeriodByTemplateId(templateId: Long): TodoPeriodModel? {
        return dataSource.getTodoPeriodByTemplateId(templateId)?.toModel()
    }
}
