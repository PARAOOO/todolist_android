package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoDayOfWeekModel

interface TodoDayOfWeekRepository {
    suspend fun insertTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeekModel)
    suspend fun insertDayOfWeekTodos(todoDayOfWeekEntities: List<TodoDayOfWeekModel>)
    suspend fun updateTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeekModel)
    suspend fun deleteTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeekModel)
    suspend fun getTodosByDayOfWeek(dayOfWeek: Int): List<TodoDayOfWeekModel>
    suspend fun getDayOfWeekByTemplateId(templateId: Long): List<TodoDayOfWeekModel>
    suspend fun deleteSpecificDayOfWeeks(templateId: Long, days: List<Int>)
    suspend fun deleteInstancesByTemplateIdAndDaysOfWeek(templateId: Long, days: List<Int>)
}