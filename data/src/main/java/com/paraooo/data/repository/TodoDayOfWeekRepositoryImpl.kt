package com.paraooo.data.repository

import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.data.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toModel

class TodoDayOfWeekRepositoryImpl(
    private val dataSource: TodoDayOfWeekLocalDataSource
) : TodoDayOfWeekRepository {
    override suspend fun insertTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeekModel) {
        dataSource.insertTodoDayOfWeek(todoDayOfWeek.toDto())
    }
    override suspend fun insertDayOfWeekTodos(todoDayOfWeekEntities: List<TodoDayOfWeekModel>) {
        dataSource.insertDayOfWeekTodos(todoDayOfWeekEntities.map { it.toDto() })
    }

    override suspend fun updateTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeekModel) {
        dataSource.updateTodoDayOfWeek(todoDayOfWeek.toDto())
    }

    override suspend fun deleteTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeekModel) {
        dataSource.deleteTodoDayOfWeek(todoDayOfWeek.toDto())
    }

    override suspend fun getTodosByDayOfWeek(dayOfWeek: Int): List<TodoDayOfWeekModel> {
        return dataSource.getTodosByDayOfWeek(dayOfWeek).map { it.toModel() }
    }

    override suspend fun getDayOfWeekByTemplateId(templateId: Long): List<TodoDayOfWeekModel> {
        return dataSource.getDayOfWeekByTemplateId(templateId).map { it.toModel() }
    }
    override suspend fun deleteSpecificDayOfWeeks(templateId: Long, days: List<Int>) {
        dataSource.deleteSpecificDayOfWeeks(templateId, days)
    }
    override suspend fun deleteInstancesByTemplateIdAndDaysOfWeek(
        templateId: Long,
        days: List<Int>
    ) {
        dataSource.deleteInstancesByTemplateIdAndDaysOfWeek(templateId, days)
    }
}
