package com.paraooo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.paraooo.data.local.entity.TodoEntity

@Dao
interface TodoDao {
    @Insert
    suspend fun insertTodo(todo: TodoEntity)

    @Query("SELECT * FROM TodoEntity WHERE date = :date ORDER BY hour ASC, minute ASC")
    suspend fun getTodoByDate(date: Long): List<TodoEntity>

    @Query("UPDATE TodoEntity SET progressAngle = :progress WHERE id = :todoId")
    suspend fun updateTodoProgress(todoId : Int, progress : Float)

    @Query("DELETE FROM TodoEntity WHERE id = :todoId")
    suspend fun deleteTodoById(todoId: Int)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Query("SELECT * FROM TodoEntity WHERE id = :todoId")
    suspend fun findTodoById(todoId : Int) : TodoEntity

}