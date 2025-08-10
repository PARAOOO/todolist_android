package com.paraooo.data.repository

import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.local.datasource.TodoInstanceLocalDataSource
import com.paraooo.local.datasource.TodoTemplateLocalDataSource
import com.paraooo.local.entity.TodoInstance
import java.time.LocalDate

class TodoRepositoryImpl(
    private val todoInstanceLocalDataSource: TodoInstanceLocalDataSource,
    private val todoTemplateLocalDataSource: TodoTemplateLocalDataSource,
) : TodoRepository {

    override suspend fun getTodoInstanceByInstanceId(instanceId: Long) : TodoInstanceModel? {
        return todoInstanceLocalDataSource.getTodoInstanceById(instanceId)?.toModel()
    }

    override suspend fun postTodo(todoTemplate: TodoTemplateModel, todoInstance: TodoInstanceModel) : Long {

        val templateId = todoTemplateLocalDataSource.insertTodoTemplate(todoTemplate.toEntity())

        todoInstanceLocalDataSource.insertTodoInstance(
            todoInstance.copy(
                templateId = templateId
            ).toEntity()
        )

        return templateId
    }

    override suspend fun updateTodo(
        todoTemplate: TodoTemplateModel,
        todoInstance: TodoInstanceModel
    ) {

        todoTemplateLocalDataSource.updateTodoTemplate(
            todoTemplate.toEntity()
        )

        todoInstanceLocalDataSource.updateTodoInstance(
            todoInstance.toEntity()
        )
    }

    override suspend fun updateTodoProgress(todoInstanceId: Long, progressAngle: Float) {
        todoInstanceLocalDataSource.updateTodoProgress(todoInstanceId, progressAngle)
    }

    override suspend fun deleteTodoTemplate(templateId: Long) {
        todoTemplateLocalDataSource.deleteTodoTemplate(templateId)
    }
}