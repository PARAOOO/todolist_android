package com.paraooo.local.datasource

import com.paraooo.local.entity.TodoEntity
import com.paraooo.local.entity.TodoTemplate
import com.paraooo.local.dao.TodoTemplateDao
import kotlinx.coroutines.flow.Flow


interface TodoTemplateLocalDataSource {

    suspend fun insertTodoTemplate(todoTemplate: TodoTemplate): Long

    suspend fun updateTodoTemplate(todoTemplate: TodoTemplate)

    suspend fun deleteTodoTemplate(templateId: Long)

    suspend fun getTodoTemplateById(id: Long): TodoTemplate?

    suspend fun getAllTodoTemplates(): List<TodoTemplate>

    suspend fun getTodosByDate(selectedDate: Long): List<TodoEntity>

    suspend fun observeTodosByDate(selectedDate: Long): Flow<List<TodoEntity>>

    suspend fun getAlarmTodos(todayMillis: Long): List<TodoEntity>

}
