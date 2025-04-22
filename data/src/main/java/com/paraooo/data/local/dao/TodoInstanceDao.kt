package com.paraooo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.paraooo.data.local.entity.TodoInstance

@Dao
interface TodoInstanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoInstance(todoInstance: TodoInstance): Long

    @Insert(onConflict = REPLACE)
    suspend fun insertTodoInstances(todoInstances: List<TodoInstance>)

    @Update
    suspend fun updateTodoInstance(todoInstance: TodoInstance)

    @Query("UPDATE todo_instance SET progressAngle = :progressAngle WHERE id = :todoInstanceId")
    suspend fun updateTodoProgress(todoInstanceId: Long, progressAngle: Float)

    @Query("DELETE FROM todo_instance WHERE id = :todoInstanceId")
    suspend fun deleteTodoInstance(todoInstanceId: Long)

    @Query("SELECT * FROM todo_instance WHERE id = :todoInstanceId")
    suspend fun getTodoInstanceById(todoInstanceId: Long): TodoInstance?

    @Query("SELECT * FROM todo_instance WHERE templateId = :templateId ORDER BY date ASC")
    suspend fun getInstancesByTemplateId(templateId: Long): List<TodoInstance>

    @Query("DELETE FROM todo_instance WHERE templateId = :templateId AND date IN (:dates)")
    suspend fun deleteInstancesByDates(templateId: Long, dates: Set<Long>)

}