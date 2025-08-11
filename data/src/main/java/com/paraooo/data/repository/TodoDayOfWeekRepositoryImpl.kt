package com.paraooo.data.repository

import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.model.TodoDayOfWeekWithTimeModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.local.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.local.datasource.TodoTemplateLocalDataSource

internal class TodoDayOfWeekRepositoryImpl(
    private val todoDayOfWeekLocalDataSource: TodoDayOfWeekLocalDataSource,
    private val todoTemplateLocalDataSource: TodoTemplateLocalDataSource,
) : TodoDayOfWeekRepository {

    override suspend fun postTodoDayOfWeek(
        todoTemplate: TodoTemplateModel,
        todoDayOfWeeks: List<TodoDayOfWeekModel>
    ): Long {
        val templateId = todoTemplateLocalDataSource.insertTodoTemplate(todoTemplate.toEntity())
        todoDayOfWeekLocalDataSource.insertDayOfWeekTodos(todoDayOfWeeks.map { it.toEntity().copy(templateId = templateId) })

        return templateId
    }

    override suspend fun updateTodoDayOfWeek(
        templateId: Long,
        todoTemplate: TodoTemplateModel,
        dayOfWeeksToDelete: List<Int>,
        dayOfWeeksToInsert: List<TodoDayOfWeekModel>
    ) {
        todoTemplateLocalDataSource.updateTodoTemplate(todoTemplate.toEntity())
        todoDayOfWeekLocalDataSource.deleteSpecificDayOfWeeks(templateId, dayOfWeeksToDelete)
        todoDayOfWeekLocalDataSource.deleteInstancesByTemplateIdAndDaysOfWeek(templateId, dayOfWeeksToDelete)
        todoDayOfWeekLocalDataSource.insertDayOfWeekTodos(dayOfWeeksToInsert.map { it.toEntity() })
    }

    override suspend fun insertTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeekModel) {
        todoDayOfWeekLocalDataSource.insertTodoDayOfWeek(todoDayOfWeek.toEntity())
    }

    override suspend fun insertDayOfWeekTodos(todoDayOfWeeks: List<TodoDayOfWeekModel>) {
        todoDayOfWeekLocalDataSource.insertDayOfWeekTodos(todoDayOfWeeks.map { it.toEntity() })
    }

    override suspend fun deleteTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeekModel) {
        todoDayOfWeekLocalDataSource.deleteTodoDayOfWeek(todoDayOfWeek.toEntity())
    }

    override suspend fun getTodosByDayOfWeek(dayOfWeek: Int): List<TodoDayOfWeekModel> {
        return todoDayOfWeekLocalDataSource.getTodosByDayOfWeek(dayOfWeek).map { it.toModel() }
    }

    override suspend fun getDayOfWeekByTemplateId(templateId: Long): List<TodoDayOfWeekModel> {
        return todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(templateId).map { it.toModel() }
    }

    override suspend fun deleteSpecificDayOfWeeks(templateId: Long, days: List<Int>) {
        todoDayOfWeekLocalDataSource.deleteSpecificDayOfWeeks(templateId, days)
    }

    override suspend fun deleteInstancesByTemplateIdAndDaysOfWeek(templateId: Long, days: List<Int>) {
        todoDayOfWeekLocalDataSource.deleteInstancesByTemplateIdAndDaysOfWeek(templateId, days)
    }

    override suspend fun getDayOfWeekTodoTemplatesByDate(date: Long): List<TodoTemplateModel> {
        return todoDayOfWeekLocalDataSource.getDayOfWeekTodoTemplatesByDate(date).map { it.toModel() }
    }

    override suspend fun getAlarmDayOfWeekTodos(): List<TodoDayOfWeekWithTimeModel> {
        return todoDayOfWeekLocalDataSource.getAlarmDayOfWeekTodos().map { it.toModel() }
    }

}
