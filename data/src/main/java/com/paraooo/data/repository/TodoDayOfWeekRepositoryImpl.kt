package com.paraooo.data.repository

import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.model.TodoDayOfWeekWithTimeModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.local.database.TransactionProvider
import com.paraooo.local.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.local.datasource.TodoTemplateLocalDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

internal class TodoDayOfWeekRepositoryImpl(
    private val todoDayOfWeekLocalDataSource: TodoDayOfWeekLocalDataSource,
    private val todoTemplateLocalDataSource: TodoTemplateLocalDataSource,
    private val transactionProvider: TransactionProvider,
) : TodoDayOfWeekRepository {

    override suspend fun postTodoDayOfWeek(
        todoTemplate: TodoTemplateModel,
        todoDayOfWeeks: List<TodoDayOfWeekModel>
    ): Long {
        return transactionProvider.runInTransaction {
            val templateId = todoTemplateLocalDataSource.insertTodoTemplate(todoTemplate.toEntity())
            todoDayOfWeekLocalDataSource.insertDayOfWeekTodos(todoDayOfWeeks.map {
                it.toEntity().copy(templateId = templateId)
            })

            templateId
        }
    }

    override suspend fun updateTodoDayOfWeek(
        templateId: Long,
        todoTemplate: TodoTemplateModel,
        dayOfWeeksToDelete: List<Int>,
        dayOfWeeksToInsert: List<TodoDayOfWeekModel>
    ) {
        transactionProvider.runInTransaction {
            coroutineScope {
                val jobs = listOf(
                    async { todoTemplateLocalDataSource.updateTodoTemplate(todoTemplate.toEntity()) },
                    async { todoDayOfWeekLocalDataSource.deleteSpecificDayOfWeeks(templateId, dayOfWeeksToDelete) },
                    async { todoDayOfWeekLocalDataSource.deleteInstancesByTemplateIdAndDaysOfWeek(templateId, dayOfWeeksToDelete) },
                    async { todoDayOfWeekLocalDataSource.insertDayOfWeekTodos(dayOfWeeksToInsert.map { it.toEntity() }) }
                )

                jobs.awaitAll()
            }
        }
    }

    override suspend fun getDayOfWeekByTemplateId(templateId: Long): List<TodoDayOfWeekModel> {
        return todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(templateId).map { it.toModel() }
    }

    override suspend fun getDayOfWeekTodoTemplatesByDate(date: Long): List<TodoTemplateModel> {
        return todoDayOfWeekLocalDataSource.getDayOfWeekTodoTemplatesByDate(date).map { it.toModel() }
    }

    override suspend fun getAlarmDayOfWeekTodos(): List<TodoDayOfWeekWithTimeModel> {
        return todoDayOfWeekLocalDataSource.getAlarmDayOfWeekTodos().map { it.toModel() }
    }

}
