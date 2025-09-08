package com.paraooo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.paraooo.local.entity.TodoInstance
import com.paraooo.local.entity.TodoTemplate
import java.util.UUID

@Dao
interface SyncTodoDao {

    @Query("SELECT * FROM todo_template WHERE needsSync = 1")
    suspend fun getUnsyncedTemplates(): List<TodoTemplate>

    @Query("UPDATE todo_template SET needsSync = 0 WHERE id IN (:ids)")
    suspend fun markTemplatesAsSynced(ids: List<UUID>)


    @Query("SELECT * FROM todo_instance WHERE needsSync = 1")
    suspend fun getUnsyncedInstances(): List<TodoInstance>

    @Query("UPDATE todo_instance SET needsSync = 0 WHERE id IN (:ids)")
    suspend fun markInstancesAsSynced(ids: List<UUID>)

    @Query("DELETE FROM todo_template WHERE id IN (:ids)")
    suspend fun deleteTemplatesByIds(ids: List<UUID>)

    @Query("DELETE FROM todo_instance WHERE id IN (:ids)")
    suspend fun deleteInstancesByIds(ids: List<UUID>)

    @Insert
    suspend fun insertTemplates(templates: List<TodoTemplate>)

    @Update
    suspend fun updateTemplates(templates: List<TodoTemplate>)

    @Query("SELECT id FROM todo_template WHERE id IN (:ids)")
    suspend fun getExistingTemplateIds(ids: List<UUID>): List<UUID>

    @Transaction
    suspend fun upsertTemplates(templates: List<TodoTemplate>) {
        if (templates.isEmpty()) return

        val incomingIds = templates.map { it.id }
        val existingIds = getExistingTemplateIds(incomingIds).toSet()

        val (toUpdate, toInsert) = templates.partition { it.id in existingIds }

        if (toUpdate.isNotEmpty()) {
            updateTemplates(toUpdate)
        }
        if (toInsert.isNotEmpty()) {
            insertTemplates(toInsert)
        }
    }

    @Insert
    suspend fun insertInstances(instances: List<TodoInstance>)

    @Update
    suspend fun updateInstances(instances: List<TodoInstance>)

    @Query("SELECT id FROM todo_instance WHERE id IN (:ids)")
    suspend fun getExistingInstanceIds(ids: List<UUID>): List<UUID>

    @Transaction
    suspend fun upsertInstances(instances: List<TodoInstance>) {
        if (instances.isEmpty()) return

        val incomingIds = instances.map { it.id }
        val existingIds = getExistingInstanceIds(incomingIds).toSet()

        val (toUpdate, toInsert) = instances.partition { it.id in existingIds }

        if (toUpdate.isNotEmpty()) {
            updateInstances(toUpdate)
        }
        if (toInsert.isNotEmpty()) {
            insertInstances(toInsert)
        }
    }

}
