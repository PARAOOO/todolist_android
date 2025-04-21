package com.paraooo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.paraooo.data.local.entity.TodoEntity
import com.paraooo.data.local.entity.TodoTemplate

@Dao
interface TodoTemplateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoTemplate(todoTemplate: TodoTemplate): Long

    @Update
    suspend fun updateTodoTemplate(todoTemplate: TodoTemplate)

    @Query("DELETE FROM todo_template WHERE id = :templateId")
    suspend fun deleteTodoTemplate(templateId: Long)

    @Query("SELECT * FROM todo_template WHERE id = :id")
    suspend fun getTodoTemplateById(id: Long): TodoTemplate?

    @Query("SELECT * FROM todo_template")
    suspend fun getAllTodoTemplates(): List<TodoTemplate>

    @Query(
        """
        SELECT 
            ti.id AS instanceId, 
            tt.id AS templateId,
            tt.title AS title,
            tt.description AS description,
            ti.date AS date,
            tt.hour AS hour,
            tt.minute AS minute,
            ti.progressAngle AS progressAngle,
            tt.alarmType AS alarmType
        FROM todo_instance AS ti
        INNER JOIN todo_template AS tt ON ti.templateId = tt.id
        WHERE ti.date = :selectedDate
        ORDER BY tt.hour ASC, tt.minute ASC
        """
    )
    suspend fun getTodosByDate(selectedDate: Long): List<TodoEntity>

    @Query("""
    SELECT 
        i.id AS instanceId,
        t.id AS templateId,
        t.title AS title,
        t.description AS description,
        i.date AS date,
        t.hour AS hour,
        t.minute AS minute,
        i.progressAngle AS progressAngle,
        t.alarmType AS alarmType
    FROM todo_template t
    INNER JOIN todo_instance i ON t.id = i.templateId
    WHERE t.alarmType != 'OFF'
      AND t.type = 'GENERAL'
      AND i.date >= :todayMillis
""")
    suspend fun getAlarmTodos(todayMillis: Long): List<TodoEntity>

}