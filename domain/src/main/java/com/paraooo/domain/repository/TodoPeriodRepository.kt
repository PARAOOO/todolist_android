package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoPeriodModel
import com.paraooo.domain.model.TodoPeriodWithTimeModel
import com.paraooo.domain.model.TodoTemplateModel
import java.time.LocalDate
import java.util.UUID

interface TodoPeriodRepository {

    suspend fun postTodoPeriod(todoTemplate : TodoTemplateModel, todoInstances : List<TodoInstanceModel>, todoPeriod : TodoPeriodModel)

    suspend fun updateTodoPeriod(templateId : UUID, todoTemplate : TodoTemplateModel, todoPeriod: TodoPeriodModel, datesToDelete : Set<Long>, todoInstancesToInsert: List<TodoInstanceModel>)

    suspend fun getTodoPeriodByTemplateId(templateId: UUID): TodoPeriodModel?

    suspend fun getAlarmPeriodTodos(todayMillis: Long): List<TodoPeriodWithTimeModel>
}