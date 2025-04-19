package com.paraooo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.paraooo.data.dto.TodoDayOfWeekAlarm
import com.paraooo.data.dto.TodoDto
import com.paraooo.data.dto.TodoPeriodAlarm
import com.paraooo.data.local.entity.TodoDayOfWeek
//import com.paraooo.data.local.entity.TodoDayOfWeek
//import com.paraooo.data.local.entity.TodoEntity
import com.paraooo.data.local.entity.TodoInstance
import com.paraooo.data.local.entity.TodoPeriod
//import com.paraooo.data.local.entity.TodoPeriod
import com.paraooo.data.local.entity.TodoTemplate
import java.time.LocalDate

@Dao
interface TodoDao {

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


    // 📌 2. TodoInstance 관련 DAO
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstances(instances: List<TodoInstance>)

    // 📌 3. TodoPeriod 관련 DAO
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoPeriod(todoPeriod: TodoPeriod)

    @Update
    suspend fun updateTodoPeriod(todoPeriod: TodoPeriod)

    @Delete
    suspend fun deleteTodoPeriod(todoPeriod: TodoPeriod)

    @Query("SELECT * FROM todo_period WHERE templateId = :templateId")
    suspend fun getTodoPeriodByTemplateId(templateId: Long): TodoPeriod?

    // 📌 4. TodoDayOfWeek 관련 DAO
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
    suspend fun getTodosByDate(selectedDate: Long): List<TodoDto>

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
    suspend fun getAlarmTodos(todayMillis: Long): List<TodoDto>

    @Query("""
        SELECT 
            template.id AS templateId,
            template.hour AS hour,
            template.minute AS minute,
            period.startDate AS startDate,
            period.endDate AS endDate
        FROM todo_template AS template
        INNER JOIN todo_period AS period ON template.id = period.templateId
        WHERE template.type = "PERIOD"
          AND template.alarmType != "OFF"
          AND period.startDate <= :todayMillis
          AND period.endDate >= :todayMillis
    """)
    suspend fun getAlarmPeriodTodos(
        todayMillis: Long
    ): List<TodoPeriodAlarm>


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