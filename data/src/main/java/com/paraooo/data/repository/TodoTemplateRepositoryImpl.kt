package com.paraooo.data.repository

import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.local.datasource.TodoTemplateLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


internal class TodoTemplateRepositoryImpl(
    private val todoTemplateLocalDataSource: TodoTemplateLocalDataSource
) : TodoTemplateRepository {

    override suspend fun getTodoTemplateById(id: Long): TodoTemplateModel? {
        return todoTemplateLocalDataSource.getTodoTemplateById(id)?.toModel()
    }

    override suspend fun getAlarmTodos(todayMillis: Long): List<TodoModel> {
        return todoTemplateLocalDataSource.getAlarmTodos(todayMillis).map { it.toModel() }
    }

}
