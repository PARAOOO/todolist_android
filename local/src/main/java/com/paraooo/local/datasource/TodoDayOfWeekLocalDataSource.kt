package com.paraooo.local.datasource

import com.paraooo.local.dao.TodoDayOfWeekDao
import com.paraooo.local.entity.TodoDayOfWeek
import com.paraooo.local.entity.TodoDayOfWeekWithTime
import com.paraooo.local.entity.TodoTemplate
import java.util.UUID

interface TodoDayOfWeekLocalDataSource {

    suspend fun insertTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeek)

    suspend fun insertDayOfWeekTodos(todoDayOfWeekEntities: List<TodoDayOfWeek>)

    suspend fun updateTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeek)

    suspend fun deleteTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeek)

    suspend fun getTodosByDayOfWeek(dayOfWeek: Int): List<TodoDayOfWeek>

    suspend fun getDayOfWeekByTemplateId(templateId: UUID): List<TodoDayOfWeek>

    suspend fun deleteSpecificDayOfWeeks(templateId: UUID, days: List<Int>)

    suspend fun deleteInstancesByTemplateIdAndDaysOfWeek(templateId: UUID, days: List<Int>)

    suspend fun getDayOfWeekTodoTemplatesByDate(date: Long): List<TodoTemplate>

    suspend fun getAlarmDayOfWeekTodos(): List<TodoDayOfWeekWithTime>

}
