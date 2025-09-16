package com.paraooo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.paraooo.local.entity.TodoEntity
import com.paraooo.local.entity.TodoTemplate
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
internal interface TodoTemplateDao {

    @Query("DELETE FROM todo_template")
    suspend fun clearTodoTemplate()

    @Query("DELETE FROM todo_instance")
    suspend fun clearTodoInstance()

    @Query("DELETE FROM todo_period")
    suspend fun clearTodoPeriod()

    @Query("DELETE FROM todo_day_of_week")
    suspend fun clearTodoDayOfWeek()

    @Query("DELETE FROM deleted_todo")
    suspend fun clearDeletedTodo()

    @Transaction
    suspend fun clearAllTables() {
        clearTodoTemplate()
        clearTodoInstance()
        clearTodoPeriod()
        clearTodoDayOfWeek()
        clearDeletedTodo()
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoTemplate(todoTemplate: TodoTemplate)

    @Update
    suspend fun updateTodoTemplate(todoTemplate: TodoTemplate)

    @Query("DELETE FROM todo_template WHERE id = :templateId")
    suspend fun deleteTodoTemplate(templateId: UUID)

    @Query("SELECT * FROM todo_template WHERE id = :id")
    suspend fun getTodoTemplateById(id: UUID): TodoTemplate?

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
            tt.alarmType AS alarmType,
            tt.isAlarmHasSound AS isAlarmHasSound,
            tt.isAlarmHasVibration AS isAlarmHasVibration
        FROM todo_instance AS ti
        INNER JOIN todo_template AS tt ON ti.templateId = tt.id
        WHERE ti.date = :selectedDate
        ORDER BY tt.hour ASC, tt.minute ASC
        """
    )
    suspend fun getTodosByDate(selectedDate: Long): List<TodoEntity>

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
            tt.alarmType AS alarmType,
            tt.isAlarmHasSound AS isAlarmHasSound,
            tt.isAlarmHasVibration AS isAlarmHasVibration
        FROM todo_instance AS ti
        INNER JOIN todo_template AS tt ON ti.templateId = tt.id
        WHERE ti.date = :selectedDate
        ORDER BY tt.hour ASC, tt.minute ASC
        """
    )
    fun observeTodosByDate(selectedDate: Long): Flow<List<TodoEntity>>


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
        t.alarmType AS alarmType,
        t.isAlarmHasSound AS isAlarmHasSound,
        t.isAlarmHasVibration AS isAlarmHasVibration
    FROM todo_template t
    INNER JOIN todo_instance i ON t.id = i.templateId
    WHERE t.alarmType != 'OFF'
      AND t.type = 'GENERAL'
      AND i.date >= :todayMillis
""")
    suspend fun getAlarmTodos(todayMillis: Long): List<TodoEntity>

}