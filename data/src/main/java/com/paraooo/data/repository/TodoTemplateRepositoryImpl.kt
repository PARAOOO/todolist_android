package com.paraooo.data.repository

import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.data.datasource.TodoTemplateLocalDataSource
import com.paraooo.data.dto.TodoDto
import com.paraooo.data.dto.TodoTemplateDto
import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoTemplateRepositoryImpl(
    private val todoTemplateLocalDataSource: TodoTemplateLocalDataSource
) : TodoTemplateRepository {

    override suspend fun insertTodoTemplate(todoTemplate: TodoTemplateModel): Long {
        return todoTemplateLocalDataSource.insertTodoTemplate(todoTemplate.toDto())
    }

    override suspend fun updateTodoTemplate(todoTemplate: TodoTemplateModel) {
        todoTemplateLocalDataSource.updateTodoTemplate(todoTemplate.toDto())
    }

    override suspend fun deleteTodoTemplate(templateId: Long) {
        todoTemplateLocalDataSource.deleteTodoTemplate(templateId)
    }

    override suspend fun getTodoTemplateById(id: Long): TodoTemplateModel? {
        return todoTemplateLocalDataSource.getTodoTemplateById(id)?.toModel()
    }

    override suspend fun getAllTodoTemplates(): List<TodoTemplateModel> {
        return todoTemplateLocalDataSource.getAllTodoTemplates().map { it.toModel() }
    }

    override suspend fun getTodosByDate(selectedDate: Long): List<TodoModel> {
        return todoTemplateLocalDataSource.getTodosByDate(selectedDate).map { it.toModel() }
    }

    override suspend fun observeTodosByDate(selectedDate: Long): Flow<List<TodoModel>> {
        return todoTemplateLocalDataSource.observeTodosByDate(selectedDate)
            .map { it.map { it.toModel() } }
    }

    override suspend fun getAlarmTodos(todayMillis: Long): List<TodoModel> {
        return todoTemplateLocalDataSource.getAlarmTodos(todayMillis).map { it.toModel() }
    }
}