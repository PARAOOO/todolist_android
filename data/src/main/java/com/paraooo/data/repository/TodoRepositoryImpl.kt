package com.paraooo.data.repository

import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.repository.FindTodoByIdResponse
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.local.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.local.datasource.TodoInstanceLocalDataSource
import com.paraooo.local.datasource.TodoPeriodLocalDataSource
import com.paraooo.local.datasource.TodoTemplateLocalDataSource
import com.paraooo.local.entity.TodoInstance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val todoInstanceLocalDataSource: TodoInstanceLocalDataSource,
    private val todoTemplateLocalDataSource: TodoTemplateLocalDataSource,
    private val todoPeriodLocalDataSource: TodoPeriodLocalDataSource,
    private val todoDayOfWeekLocalDataSource: TodoDayOfWeekLocalDataSource
) : TodoRepository {

    override suspend fun getTodoInstanceById(instanceId: Long) : TodoInstanceModel? {
        return todoInstanceLocalDataSource.getTodoInstanceById(instanceId)?.toModel()
    }

        override suspend fun findTodoById(instanceId: Long): FindTodoByIdResponse {
            val instance = todoInstanceLocalDataSource.getTodoInstanceById(instanceId)
            val template = todoTemplateLocalDataSource.getTodoTemplateById(instance!!.templateId)
            val period = todoPeriodLocalDataSource.getTodoPeriodByTemplateId(instance.templateId)
            val dayOfWeek = todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(instance.templateId)

            return FindTodoByIdResponse(
                todoInstance = instance.toModel(),
                todoTemplate = template!!.toModel(),
                todoPeriod = period?.toModel(),
                todoDayOfWeek = dayOfWeek.map { it.toModel() }
            )
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

    override suspend fun syncDayOfWeekInstance(date: Long) {

        val currentList = todoTemplateLocalDataSource.getTodosByDate(date)

        val existingTemplateIds = currentList.map { it.templateId }.toSet()
        val dayOfWeekTemplates = todoDayOfWeekLocalDataSource.getDayOfWeekTodoTemplatesByDate(date)
        val newInstances = dayOfWeekTemplates.filterNot { existingTemplateIds.contains(it.id) }.map {
            TodoInstance(templateId = it.id, date = date)
        }

        todoInstanceLocalDataSource.insertTodoInstances(newInstances)
    }

    override suspend fun observeTodosByDate(date: Long): Flow<List<TodoModel>> {
        return todoTemplateLocalDataSource.observeTodosByDate(date).map { it.map { it.toModel() } }
    }
}