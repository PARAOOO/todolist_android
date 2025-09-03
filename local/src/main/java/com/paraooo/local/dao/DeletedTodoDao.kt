package com.paraooo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.paraooo.local.entity.DeletedTodo
import java.util.UUID

@Dao
interface DeletedTodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deletedTodo: DeletedTodo)

    @Query("SELECT * FROM deleted_todo WHERE itemType = 'TEMPLATE'")
    suspend fun getDeletedTemplates(): List<DeletedTodo>

    @Query("SELECT * FROM deleted_todo WHERE itemType = 'INSTANCE'")
    suspend fun getDeletedInstances(): List<DeletedTodo>

    @Query("DELETE FROM deleted_todo WHERE id IN (:ids)")
    suspend fun deleteByIds(ids: List<UUID>)
}