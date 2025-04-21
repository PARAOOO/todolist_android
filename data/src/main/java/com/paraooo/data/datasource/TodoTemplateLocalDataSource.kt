package com.paraooo.data.datasource

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.paraooo.data.dto.TodoDto
import com.paraooo.data.dto.TodoTemplateDto
import com.paraooo.data.local.dao.TodoTemplateDao
import com.paraooo.data.local.entity.TodoEntity
import com.paraooo.data.local.entity.TodoTemplate
import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toEntity

//import com.paraooo.data.local.entity.TodoEntity
//import com.paraooo.data.mapper.toDto
//import com.paraooo.data.mapper.toEntity

class TodoTemplateLocalDataSource(
    private val todoTemplateDao: TodoTemplateDao
) {

    suspend fun insertTodoTemplate(todoTemplate: TodoTemplateDto): Long {
        return todoTemplateDao.insertTodoTemplate(todoTemplate.toEntity())
    }

    suspend fun updateTodoTemplate(todoTemplate: TodoTemplateDto) {
        todoTemplateDao.updateTodoTemplate(todoTemplate.toEntity())
    }

    suspend fun deleteTodoTemplate(templateId: Long) {
        todoTemplateDao.deleteTodoTemplate(templateId)
    }

    suspend fun getTodoTemplateById(id: Long): TodoTemplateDto? {
        return todoTemplateDao.getTodoTemplateById(id)?.toDto()
    }

    suspend fun getAllTodoTemplates(): List<TodoTemplateDto> {
        return todoTemplateDao.getAllTodoTemplates().map { it.toDto() }
    }

    suspend fun getTodosByDate(selectedDate: Long): List<TodoDto> {
        return todoTemplateDao.getTodosByDate(selectedDate).map { it.toDto() }
    }

    suspend fun getAlarmTodos(todayMillis: Long): List<TodoDto> {
        return todoTemplateDao.getAlarmTodos(todayMillis).map { it.toDto() }
    }

}
