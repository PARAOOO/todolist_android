package com.paraooo.data.repository

import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.repository.FindTodoByIdResponse
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.local.database.TransactionProvider
import com.paraooo.local.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.local.datasource.TodoInstanceLocalDataSource
import com.paraooo.local.datasource.TodoPeriodLocalDataSource
import com.paraooo.local.datasource.TodoTemplateLocalDataSource
import com.paraooo.local.entity.TodoInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepositoryImpl(
    private val todoInstanceLocalDataSource: TodoInstanceLocalDataSource,
    private val todoTemplateLocalDataSource: TodoTemplateLocalDataSource,
    private val todoPeriodLocalDataSource: TodoPeriodLocalDataSource,
    private val todoDayOfWeekLocalDataSource: TodoDayOfWeekLocalDataSource,
    private val transactionProvider: TransactionProvider,
) : TodoRepository {

    override suspend fun getTodoInstanceById(instanceId: Long) : TodoInstanceModel? {
        return todoInstanceLocalDataSource.getTodoInstanceById(instanceId)?.toModel()
    }

    override suspend fun findTodoById(instanceId: Long): FindTodoByIdResponse? {
        val instance = todoInstanceLocalDataSource.getTodoInstanceById(instanceId) ?: return null

        return transactionProvider.runInTransaction {
            coroutineScope {
                val templateDeferred = async { todoTemplateLocalDataSource.getTodoTemplateById(instance.templateId) }
                val periodDeferred = async { todoPeriodLocalDataSource.getTodoPeriodByTemplateId(instance.templateId) }
                val dayOfWeekDeferred = async { todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(instance.templateId) }

                val template = templateDeferred.await() ?: return@coroutineScope null

                FindTodoByIdResponse(
                    todoInstance = instance.toModel(),
                    todoTemplate = template.toModel(),
                    todoPeriod = periodDeferred.await()?.toModel(),
                    todoDayOfWeek = dayOfWeekDeferred.await().map { it.toModel() }
                )
            }
        }
    }

    override suspend fun postTodo(todoTemplate: TodoTemplateModel, todoInstance: TodoInstanceModel) : Long {

        return transactionProvider.runInTransaction {
            val templateId = todoTemplateLocalDataSource.insertTodoTemplate(todoTemplate.toEntity())

            todoInstanceLocalDataSource.insertTodoInstance(
                todoInstance.copy(
                    templateId = templateId
                ).toEntity()
            )

            templateId
        }
    }

    override suspend fun updateTodo(
        todoTemplate: TodoTemplateModel,
        todoInstance: TodoInstanceModel
    ) {
        transactionProvider.runInTransaction {
            coroutineScope {
                val jobs = listOf(
                    async { todoTemplateLocalDataSource.updateTodoTemplate(todoTemplate.toEntity()) },
                    async { todoInstanceLocalDataSource.updateTodoInstance(todoInstance.toEntity()) },
                )
                jobs.awaitAll()
            }
        }
    }

    override suspend fun updateTodoProgress(todoInstanceId: Long, progressAngle: Float) {
        todoInstanceLocalDataSource.updateTodoProgress(todoInstanceId, progressAngle)
    }

    override suspend fun deleteTodoTemplate(templateId: Long) {
        todoTemplateLocalDataSource.deleteTodoTemplate(templateId)
    }

    override suspend fun syncDayOfWeekInstance(todoInstances: List<TodoInstanceModel>) {
        todoInstanceLocalDataSource.insertTodoInstances(todoInstances.map { it.toEntity() })
    }

    override suspend fun observeTodosByDate(date: Long): Flow<List<TodoModel>> {
        return todoTemplateLocalDataSource.observeTodosByDate(date).map { it.map { it.toModel() } }
    }
}