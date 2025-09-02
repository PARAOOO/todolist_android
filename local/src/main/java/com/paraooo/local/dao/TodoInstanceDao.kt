package com.paraooo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.paraooo.local.entity.TodoInstance
import java.util.UUID

@Dao
internal interface TodoInstanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoInstance(todoInstance: TodoInstance)

    @Insert(onConflict = REPLACE)
    suspend fun insertTodoInstances(todoInstances: List<TodoInstance>)

    @Update
    suspend fun updateTodoInstance(todoInstance: TodoInstance)

    @Query("UPDATE todo_instance SET progressAngle = :progressAngle WHERE id = :todoInstanceId")
    suspend fun updateTodoProgress(todoInstanceId: UUID, progressAngle: Float)

    @Query("DELETE FROM todo_instance WHERE id = :todoInstanceId")
    suspend fun deleteTodoInstance(todoInstanceId: UUID)

    @Query("SELECT * FROM todo_instance WHERE id = :todoInstanceId")
    suspend fun getTodoInstanceById(todoInstanceId: UUID): TodoInstance?

    @Query("SELECT * FROM todo_instance WHERE templateId = :templateId ORDER BY date ASC")
    suspend fun getInstancesByTemplateId(templateId: UUID): List<TodoInstance>

    @Query("DELETE FROM todo_instance WHERE templateId = :templateId AND date IN (:dates)")
    suspend fun deleteInstancesByDates(templateId: UUID, dates: Set<Long>)

}