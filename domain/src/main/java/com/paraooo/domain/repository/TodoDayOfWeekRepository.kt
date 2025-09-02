package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.model.TodoDayOfWeekWithTimeModel
import com.paraooo.domain.model.TodoTemplateModel
import java.util.UUID

interface TodoDayOfWeekRepository {

    suspend fun postTodoDayOfWeek(todoTemplate: TodoTemplateModel, todoDayOfWeeks: List<TodoDayOfWeekModel>)

    suspend fun updateTodoDayOfWeek(templateId: UUID, todoTemplate: TodoTemplateModel, dayOfWeeksToDelete: List<Int>, dayOfWeeksToInsert: List<TodoDayOfWeekModel>)

    suspend fun getDayOfWeekByTemplateId(templateId: UUID): List<TodoDayOfWeekModel>

    suspend fun getDayOfWeekTodoTemplatesByDate(date: Long): List<TodoTemplateModel>

    suspend fun getAlarmDayOfWeekTodos(): List<TodoDayOfWeekWithTimeModel>

}
