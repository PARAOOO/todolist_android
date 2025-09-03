package com.paraooo.local.dao

import androidx.room.Dao
import androidx.room.Query
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

}