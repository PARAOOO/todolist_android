package com.paraooo.data.datasource

import com.paraooo.data.dto.TodoDto
import com.paraooo.data.local.dao.TodoTemplateDao
//import com.paraooo.data.local.entity.TodoEntity
//import com.paraooo.data.mapper.toDto
//import com.paraooo.data.mapper.toEntity

class TodoTemplateLocalDataSource(
    private val todoTemplateDao: TodoTemplateDao
) {

    suspend fun insertTodoTemplate(todoTemplate: TodoDto) {
        todoTemplateDao.insertTodoTemplate(todoTemplate.toEntity())
    }

    suspend fun updateTodoTemplate(todoTemplate: TodoDto) {
        todoTemplateDao.updateTodoTemplate(todoTemplate.toEntity())
    }

    suspend fun deleteTodoTemplate(todoTemplate: TodoDto) {
        todoTemplateDao.deleteTodoTemplate(todoTemplate.toEntity())
    }

    suspend fun getTodoTemplateById(id: Long): TodoDto? {
        return todoTemplateDao.getTodoTemplateById(id)?.toDto()
    }

    suspend fun getAllTodoTemplates(): List<TodoDto> {
        return todoTemplateDao.getAllTodoTemplates().map { it.toDto() }
    }

    suspend fun getTodosByDate(selectedDate: Long): List<TodoDto> {
        return todoTemplateDao.getTodosByDate(selectedDate).map { it.toDto() }
    }

    suspend fun getAlarmTodos(todayMillis: Long): List<TodoDto> {
        return todoTemplateDao.getAlarmTodos(todayMillis).map { it.toDto() }
    }

}