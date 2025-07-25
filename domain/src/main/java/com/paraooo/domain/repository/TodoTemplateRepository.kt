package com.paraooo.domain.repository

import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import kotlinx.coroutines.flow.Flow


interface TodoTemplateRepository {

    suspend fun insertTodoTemplate(todoTemplate: TodoTemplateModel): Long

    suspend fun updateTodoTemplate(todoTemplate: TodoTemplateModel)

    suspend fun deleteTodoTemplate(templateId: Long)

    suspend fun getTodoTemplateById(id: Long): TodoTemplateModel?

    suspend fun getAllTodoTemplates(): List<TodoTemplateModel>

    suspend fun getTodosByDate(selectedDate: Long): List<TodoModel>

    suspend fun observeTodosByDate(selectedDate: Long): Flow<List<TodoModel>>

    suspend fun getAlarmTodos(todayMillis: Long): List<TodoModel>

}
