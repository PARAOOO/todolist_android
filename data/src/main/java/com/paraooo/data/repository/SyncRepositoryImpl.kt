package com.paraooo.data.repository

import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.repository.SyncRepository
import com.paraooo.local.database.TransactionProvider
import com.paraooo.local.datasource.DeletedTodoLocalDataSource
import com.paraooo.local.datasource.SyncTodoLocalDataSource
import com.paraooo.remote.datasource.SyncRemoteDataSource
import com.paraooo.remote.dto.request.SyncRequestDto
import java.util.UUID

class SyncRepositoryImpl(
    private val syncRemoteDataSource: SyncRemoteDataSource,
    private val syncTodoLocalDataSource: SyncTodoLocalDataSource,
    private val deletedTodoLocalDataSource: DeletedTodoLocalDataSource,
    private val transactionProvider: TransactionProvider,
): SyncRepository {

    override suspend fun syncPush(
        templates: List<TodoTemplateModel>,
        instances: List<TodoInstanceModel>,
        deletedTemplateIds: List<UUID>,
        deletedInstanceIds: List<UUID>
    ) {
        transactionProvider.runInTransaction {
            syncRemoteDataSource.syncPush(
                request = SyncRequestDto(
                    templates = templates.map { it.toDto() },
                    instances = instances.map { it.toDto() },
                    deletedTemplateUuids = deletedTemplateIds.map { it.toString() },
                    deletedInstanceUuids = deletedInstanceIds.map { it.toString() }
                )
            )

            syncTodoLocalDataSource.markTemplatesAsSynced(templates.map { it.id })
            syncTodoLocalDataSource.markInstancesAsSynced(instances.map { it.id })

            deletedTodoLocalDataSource.deleteByIds(deletedTemplateIds)
            deletedTodoLocalDataSource.deleteByIds(deletedInstanceIds)
        }
    }

    override suspend fun getUnsyncedTemplates(): List<TodoTemplateModel> {
        return syncTodoLocalDataSource.getUnsyncedTemplates().map { it.toModel() }
    }

    override suspend fun markTemplatesAsSynced(ids: List<UUID>) {
        syncTodoLocalDataSource.markTemplatesAsSynced(ids)
    }

    override suspend fun getUnsyncedInstances(): List<TodoInstanceModel> {
        return syncTodoLocalDataSource.getUnsyncedInstances().map { it.toModel() }
    }

    override suspend fun markInstancesAsSynced(ids: List<UUID>) {
        syncTodoLocalDataSource.markInstancesAsSynced(ids)
    }

    override suspend fun getDeletedTemplates(): List<UUID> {
        return deletedTodoLocalDataSource.getDeletedTemplates().map { it.id }
    }

    override suspend fun getDeletedInstances(): List<UUID> {
        return deletedTodoLocalDataSource.getDeletedInstances().map { it.id }
    }

    override suspend fun deleteByIds(ids: List<UUID>) {
        deletedTodoLocalDataSource.deleteByIds(ids)
    }
}