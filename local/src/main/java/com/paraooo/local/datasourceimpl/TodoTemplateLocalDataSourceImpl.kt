package com.paraooo.local.datasourceimpl

import com.paraooo.local.entity.TodoEntity
import com.paraooo.local.entity.TodoTemplate
import com.paraooo.local.dao.TodoTemplateDao
import com.paraooo.local.datasource.TodoTemplateLocalDataSource
import kotlinx.coroutines.flow.Flow


internal class TodoTemplateLocalDataSourceImpl(
    private val todoTemplateDao: TodoTemplateDao
) : TodoTemplateLocalDataSource {

    override suspend fun insertTodoTemplate(todoTemplate: TodoTemplate): Long {
        return todoTemplateDao.insertTodoTemplate(todoTemplate)
    }

    override suspend fun updateTodoTemplate(todoTemplate: TodoTemplate) {
        todoTemplateDao.updateTodoTemplate(todoTemplate)
    }

    override suspend fun deleteTodoTemplate(templateId: Long) {
        todoTemplateDao.deleteTodoTemplate(templateId)
    }

    override suspend fun getTodoTemplateById(id: Long): TodoTemplate? {
        return todoTemplateDao.getTodoTemplateById(id)
    }

    override suspend fun getAllTodoTemplates(): List<TodoTemplate> {
        return todoTemplateDao.getAllTodoTemplates()
    }

    override suspend fun getTodosByDate(selectedDate: Long): List<TodoEntity> {
        return todoTemplateDao.getTodosByDate(selectedDate)
    }

    override suspend fun observeTodosByDate(selectedDate: Long): Flow<List<TodoEntity>> {
        return todoTemplateDao.observeTodosByDate(selectedDate)
    }


    override suspend fun getAlarmTodos(todayMillis: Long): List<TodoEntity> {
        return todoTemplateDao.getAlarmTodos(todayMillis)
    }

}
