package com.paraooo.data.repository

import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoPeriodModel
import com.paraooo.domain.model.TodoPeriodWithTimeModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.local.database.TransactionProvider
import com.paraooo.local.datasource.TodoInstanceLocalDataSource
import com.paraooo.local.datasource.TodoPeriodLocalDataSource
import com.paraooo.local.datasource.TodoTemplateLocalDataSource
import com.paraooo.local.entity.TodoPeriod
import com.paraooo.local.entity.TodoPeriodWithTime
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate

internal class TodoPeriodRepositoryImpl(
    private val todoTemplateLocalDataSource: TodoTemplateLocalDataSource,
    private val todoInstanceLocalDataSource: TodoInstanceLocalDataSource,
    private val todoPeriodLocalDataSource: TodoPeriodLocalDataSource,
    private val transactionProvider: TransactionProvider,
) : TodoPeriodRepository {

    override suspend fun postTodoPeriod(
        todoTemplate: TodoTemplateModel,
        todoInstances: List<TodoInstanceModel>,
        todoPeriod: TodoPeriodModel
    ) {
        transactionProvider.runInTransaction {
            val templateId = todoTemplateLocalDataSource.insertTodoTemplate(todoTemplate.toEntity())

            coroutineScope {
                val jobs = listOf(
                    async {
                        todoInstanceLocalDataSource.insertTodoInstances(todoInstances.map { it.toEntity().copy(templateId = templateId) })
                    },
                    async {
                        todoPeriodLocalDataSource.insertTodoPeriod(todoPeriod.toEntity().copy(templateId = templateId))
                    }
                )

                jobs.awaitAll()
            }
        }
    }

    override suspend fun updateTodoPeriod(
        templateId: Long,
        todoTemplate: TodoTemplateModel,
        todoPeriod: TodoPeriodModel,
        datesToDelete: Set<Long>,
        todoInstancesToInsert: List<TodoInstanceModel>
    ) {
        transactionProvider.runInTransaction {
            coroutineScope {
                val jobs = listOf(
                    async { todoTemplateLocalDataSource.updateTodoTemplate(todoTemplate = todoTemplate.toEntity()) },
                    async { todoPeriodLocalDataSource.updateTodoPeriod(todoPeriod = todoPeriod.toEntity()) },
                    async { todoInstanceLocalDataSource.deleteInstancesByDates(templateId, datesToDelete) },
                    async { todoInstanceLocalDataSource.insertTodoInstances(todoInstancesToInsert.map { it.toEntity() }) }
                )
                jobs.awaitAll()
            }
        }
    }

    override suspend fun getTodoPeriodByTemplateId(templateId: Long): TodoPeriodModel? {
        return todoPeriodLocalDataSource.getTodoPeriodByTemplateId(templateId)?.toModel()
    }

    override suspend fun getAlarmPeriodTodos(todayMillis: Long): List<TodoPeriodWithTimeModel> {
        return todoPeriodLocalDataSource.getAlarmPeriodTodos(todayMillis).map { it.toModel() }
    }
}