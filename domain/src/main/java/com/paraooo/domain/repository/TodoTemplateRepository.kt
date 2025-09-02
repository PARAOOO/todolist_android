package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID


interface TodoTemplateRepository {

    suspend fun getTodoTemplateById(id: UUID): TodoTemplateModel?
    suspend fun getTodosByDate(date: Long): List<TodoModel>
    suspend fun getAlarmTodos(todayMillis: Long): List<TodoModel>

}
