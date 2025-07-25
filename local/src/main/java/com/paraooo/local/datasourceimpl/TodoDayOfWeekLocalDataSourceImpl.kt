package com.paraooo.local.datasourceimpl

import com.paraooo.local.dao.TodoDayOfWeekDao
import com.paraooo.local.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.local.entity.TodoDayOfWeek
import com.paraooo.local.entity.TodoDayOfWeekWithTime
import com.paraooo.local.entity.TodoTemplate

internal class TodoDayOfWeekLocalDataSourceImpl(
    private val todoDayOfWeekDao: TodoDayOfWeekDao
) : TodoDayOfWeekLocalDataSource {
    override suspend fun insertTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeek) {
        todoDayOfWeekDao.insertTodoDayOfWeek(todoDayOfWeek)
    }

    override suspend fun insertDayOfWeekTodos(todoDayOfWeekEntities: List<TodoDayOfWeek>) {
        todoDayOfWeekDao.insertDayOfWeekTodos(todoDayOfWeekEntities)
    }

    override suspend fun updateTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeek) {
        todoDayOfWeekDao.updateTodoDayOfWeek(todoDayOfWeek)
    }

    override suspend fun deleteTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeek) {
        todoDayOfWeekDao.deleteTodoDayOfWeek(todoDayOfWeek)
    }

    override suspend fun getTodosByDayOfWeek(dayOfWeek: Int): List<TodoDayOfWeek> {
        return todoDayOfWeekDao.getTodosByDayOfWeek(dayOfWeek)
    }

    override suspend fun getDayOfWeekByTemplateId(templateId: Long): List<TodoDayOfWeek> {
        return todoDayOfWeekDao.getDayOfWeekByTemplateId(templateId)
    }

    override suspend fun deleteSpecificDayOfWeeks(templateId: Long, days: List<Int>) {
        todoDayOfWeekDao.deleteSpecificDayOfWeeks(templateId, days)
    }

    override suspend fun deleteInstancesByTemplateIdAndDaysOfWeek(templateId: Long, days: List<Int>) {
        todoDayOfWeekDao.deleteInstancesByTemplateIdAndDaysOfWeek(templateId, days)
    }

    override suspend fun getDayOfWeekTodoTemplatesByDate(date: Long): List<TodoTemplate> {
        return todoDayOfWeekDao.getDayOfWeekTodoTemplatesByDate(date)
    }

    override suspend fun getAlarmDayOfWeekTodos(): List<TodoDayOfWeekWithTime> {
        return todoDayOfWeekDao.getAlarmDayOfWeekTodos()
    }

}
