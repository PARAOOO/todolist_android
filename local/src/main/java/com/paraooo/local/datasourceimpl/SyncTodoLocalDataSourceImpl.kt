package com.paraooo.local.datasourceimpl

import com.paraooo.local.dao.SyncTodoDao
import com.paraooo.local.datasource.SyncTodoLocalDataSource
import com.paraooo.local.entity.TodoInstance
import com.paraooo.local.entity.TodoTemplate
import java.util.UUID

class SyncTodoLocalDataSourceImpl(
    private val syncTodoDao: SyncTodoDao
): SyncTodoLocalDataSource {

    override suspend fun getUnsyncedTemplates(): List<TodoTemplate> {
        return syncTodoDao.getUnsyncedTemplates()
    }

    override suspend fun markTemplatesAsSynced(ids: List<UUID>) {
        syncTodoDao.markTemplatesAsSynced(ids)
    }

    override suspend fun getUnsyncedInstances(): List<TodoInstance> {
        return syncTodoDao.getUnsyncedInstances()
    }

    override suspend fun markInstancesAsSynced(ids: List<UUID>) {
        syncTodoDao.markInstancesAsSynced(ids)
    }

    override suspend fun upsertTemplates(templates: List<TodoTemplate>) {
        syncTodoDao.upsertTemplates(templates)
    }

    override suspend fun deleteTemplatesByIds(ids: List<UUID>) {
        syncTodoDao.deleteTemplatesByIds(ids)
    }

    override suspend fun upsertInstances(instances: List<TodoInstance>) {
        syncTodoDao.upsertInstances(instances)
    }

    override suspend fun deleteInstancesByIds(ids: List<UUID>) {
        syncTodoDao.deleteInstancesByIds(ids)
    }


}