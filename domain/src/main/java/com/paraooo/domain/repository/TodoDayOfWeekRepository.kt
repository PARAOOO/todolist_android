package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.model.TodoDayOfWeekWithTimeModel
import com.paraooo.domain.model.TodoTemplateModel

interface TodoDayOfWeekRepository {

    suspend fun postTodoDayOfWeek(todoTemplate: TodoTemplateModel, todoDayOfWeeks: List<TodoDayOfWeekModel>) : Long

    suspend fun updateTodoDayOfWeek(templateId: Long, todoTemplate: TodoTemplateModel, dayOfWeeksToDelete: List<Int>, dayOfWeeksToInsert: List<TodoDayOfWeekModel>)

    suspend fun getDayOfWeekByTemplateId(templateId: Long): List<TodoDayOfWeekModel>

    suspend fun getAlarmDayOfWeekTodos(): List<TodoDayOfWeekWithTimeModel>

}
