package com.paraooo.local.datasource

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraooo.local.entity.DeletedTodo
import java.util.UUID

interface DeletedTodoLocalDataSource {

//    suspend fun insert(deletedTodo: UUID)

    suspend fun getDeletedTemplates(): List<DeletedTodo>

    suspend fun getDeletedInstances(): List<DeletedTodo>

    suspend fun deleteByIds(ids: List<UUID>)

}