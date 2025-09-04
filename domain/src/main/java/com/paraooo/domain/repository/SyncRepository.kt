package com.paraooo.domain.repository

import com.paraooo.domain.model.InstanceTodoSyncModel
import com.paraooo.domain.model.TemplateTodoSyncModel
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoSyncModel
import com.paraooo.domain.model.TodoTemplateModel
import java.util.UUID

interface SyncRepository {

    suspend fun syncPush(
        templates: List<TodoTemplateModel>,
        instances: List<TodoInstanceModel>,
        deletedTemplateIds: List<UUID>,
        deletedInstanceIds: List<UUID>
    )
    suspend fun getUnsyncedTemplates(): List<TodoTemplateModel>

    suspend fun markTemplatesAsSynced(ids: List<UUID>)

    suspend fun getUnsyncedInstances(): List<TodoInstanceModel>

    suspend fun markInstancesAsSynced(ids: List<UUID>)

    suspend fun getDeletedTemplates(): List<UUID>
    suspend fun getDeletedInstances(): List<UUID>

    suspend fun deleteTombstonesByIds(ids: List<UUID>)

    suspend fun syncPull(): TodoSyncModel

    suspend fun handleSyncPullResponse(
        templatesToUpsert: List<TemplateTodoSyncModel>,
        instancesToUpsert: List<InstanceTodoSyncModel>,
        templatesToDelete: List<TemplateTodoSyncModel>,
        instancesToDelete: List<InstanceTodoSyncModel>,
        newSyncTimestamp: String,
    )
}

