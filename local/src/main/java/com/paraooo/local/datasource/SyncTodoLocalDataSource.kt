package com.paraooo.local.datasource

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraooo.local.entity.TodoInstance
import com.paraooo.local.entity.TodoTemplate
import java.util.UUID

interface SyncTodoLocalDataSource {


    suspend fun getUnsyncedTemplates(): List<TodoTemplate>
    suspend fun markTemplatesAsSynced(ids: List<UUID>)
    suspend fun getUnsyncedInstances(): List<TodoInstance>
    suspend fun markInstancesAsSynced(ids: List<UUID>)


    suspend fun upsertTemplates(templates: List<TodoTemplate>)

    suspend fun deleteTemplatesByIds(ids: List<UUID>)

    suspend fun upsertInstances(instances: List<TodoInstance>)

    suspend fun deleteInstancesByIds(ids: List<UUID>)

}