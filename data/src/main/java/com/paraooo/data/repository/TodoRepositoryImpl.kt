package com.paraooo.data.repository

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.paraooo.data.mapper.toEntity
import com.paraooo.data.mapper.toModel
import com.paraooo.data.platform.alarm.AlarmWorker
import com.paraooo.data.platform.sync.SyncPushScheduler
import com.paraooo.data.platform.sync.SyncPushWorker
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.repository.FindTodoByIdResponse
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.local.dao.DeletedTodoDao
import com.paraooo.local.database.TransactionProvider
import com.paraooo.local.datasource.DeletedTodoLocalDataSource
import com.paraooo.local.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.local.datasource.TodoInstanceLocalDataSource
import com.paraooo.local.datasource.TodoPeriodLocalDataSource
import com.paraooo.local.datasource.TodoTemplateLocalDataSource
import com.paraooo.local.entity.DeletedTodo
import com.paraooo.local.entity.TodoInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import java.util.concurrent.TimeUnit

class TodoRepositoryImpl(
    private val todoInstanceLocalDataSource: TodoInstanceLocalDataSource,
    private val todoTemplateLocalDataSource: TodoTemplateLocalDataSource,
    private val todoPeriodLocalDataSource: TodoPeriodLocalDataSource,
    private val todoDayOfWeekLocalDataSource: TodoDayOfWeekLocalDataSource,
    private val deletedTodoLocalDataSource: DeletedTodoLocalDataSource,
    private val transactionProvider: TransactionProvider,
    private val syncPushScheduler: SyncPushScheduler,
) : TodoRepository {

    override suspend fun getTodoInstanceById(instanceId: UUID) : TodoInstanceModel? {
        return todoInstanceLocalDataSource.getTodoInstanceById(instanceId)?.toModel()
    }

    override suspend fun findTodoById(instanceId: UUID): FindTodoByIdResponse? {
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

    override suspend fun postTodo(todoTemplate: TodoTemplateModel, todoInstance: TodoInstanceModel) {

        transactionProvider.runInTransaction {
            todoTemplateLocalDataSource.insertTodoTemplate(todoTemplate.toEntity())

            todoInstanceLocalDataSource.insertTodoInstance(
                todoInstance.copy(
                    templateId = todoTemplate.id
                ).toEntity()
            )
        }

        syncPushScheduler.runSyncPushWorker()
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

        syncPushScheduler.runSyncPushWorker()
    }

    override suspend fun updateTodoProgress(todoInstanceId: UUID, progressAngle: Float) {
        todoInstanceLocalDataSource.updateTodoProgress(todoInstanceId, progressAngle)

        syncPushScheduler.runSyncPushWorker()
    }

    override suspend fun deleteTodoTemplate(templateId: UUID) {

        transactionProvider.runInTransaction {
            todoTemplateLocalDataSource.deleteTodoTemplate(templateId)
        }

        syncPushScheduler.runSyncPushWorker()
    }

    override suspend fun syncDayOfWeekInstance(todoInstances: List<TodoInstanceModel>) {
        todoInstanceLocalDataSource.insertTodoInstances(todoInstances.map { it.toEntity() })

        syncPushScheduler.runSyncPushWorker()
    }

    override suspend fun observeTodosByDate(date: Long): Flow<List<TodoModel>> {
        return todoTemplateLocalDataSource.observeTodosByDate(date).map { it.map { it.toModel() } }
    }
}