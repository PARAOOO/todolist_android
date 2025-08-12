package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import kotlinx.coroutines.flow.Flow


interface TodoTemplateRepository {

    suspend fun getTodoTemplateById(id: Long): TodoTemplateModel?
    suspend fun getAlarmTodos(todayMillis: Long): List<TodoModel>

}
