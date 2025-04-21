package com.paraooo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.paraooo.data.dto.TodoDayOfWeekAlarm
import com.paraooo.data.local.entity.TodoDayOfWeek
import com.paraooo.data.local.entity.TodoTemplate

@Dao
interface DayOfWeekTodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeek)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDayOfWeekTodos(todoDayOfWeekEntities: List<TodoDayOfWeek>)

    @Update
    suspend fun updateTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeek)

    @Delete
    suspend fun deleteTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeek)

    @Query("SELECT * FROM todo_day_of_week WHERE dayOfWeek = :dayOfWeek")
    suspend fun getTodosByDayOfWeek(dayOfWeek: Int): List<TodoDayOfWeek>

    @Query("SELECT * FROM todo_day_of_week WHERE templateId = :templateId")
    suspend fun getDayOfWeekByTemplateId(templateId: Long): List<TodoDayOfWeek>

    @Query("DELETE FROM todo_day_of_week WHERE templateId = :templateId AND dayOfWeek IN (:days)")
    suspend fun deleteSpecificDayOfWeeks(templateId: Long, days: List<Int>)

    @Query("""
    DELETE FROM todo_instance
    WHERE templateId = :templateId
    AND CAST(strftime('%w', date / 1000, 'unixepoch') AS INTEGER) + 1 IN (:days)
""")
    suspend fun deleteInstancesByTemplateIdAndDaysOfWeek(templateId: Long, days: List<Int>)

    @Query(
        """
        SELECT tt.*
        FROM todo_template AS tt
        JOIN todo_day_of_week AS tdw ON tt.id = tdw.templateId
        WHERE tdw.dayOfWeek = CAST(strftime('%w', :date / 1000, 'unixepoch') AS INTEGER) + 1
        """
    )
    suspend fun getDayOfWeekTodoTemplatesByDate(date: Long): List<TodoTemplate>

    @Query("""
    SELECT 
        template.id AS templateId, 
        template.hour AS hour, 
        template.minute AS minute, 
        dow.dayOfWeeks AS dayOfWeeks
    FROM todo_template AS template
    INNER JOIN (
        SELECT * FROM todo_day_of_week
        WHERE id IN (
            SELECT MIN(id)
            FROM todo_day_of_week
            GROUP BY templateId
        )
    ) AS dow ON dow.templateId = template.id
    WHERE template.type = 'DAY_OF_WEEK' AND template.alarmType != 'OFF'
""")
    suspend fun getAlarmDayOfWeekTodos(): List<TodoDayOfWeekAlarm>

}