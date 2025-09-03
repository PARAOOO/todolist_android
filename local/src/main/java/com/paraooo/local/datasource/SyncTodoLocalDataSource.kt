package com.paraooo.local.datasource

import androidx.room.Query
import com.paraooo.local.entity.TodoInstance
import com.paraooo.local.entity.TodoTemplate
import java.util.UUID

interface SyncTodoLocalDataSource {


    suspend fun getUnsyncedTemplates(): List<TodoTemplate>

    suspend fun markTemplatesAsSynced(ids: List<UUID>)

    suspend fun getUnsyncedInstances(): List<TodoInstance>

    suspend fun markInstancesAsSynced(ids: List<UUID>)

}