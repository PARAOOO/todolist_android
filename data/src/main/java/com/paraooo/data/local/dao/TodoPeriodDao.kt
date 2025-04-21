package com.paraooo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.paraooo.data.dto.TodoPeriodAlarm
import com.paraooo.data.local.entity.TodoPeriod

@Dao
interface TodoPeriodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoPeriod(todoPeriod: TodoPeriod)

    @Update
    suspend fun updateTodoPeriod(todoPeriod: TodoPeriod)

    @Delete
    suspend fun deleteTodoPeriod(todoPeriod: TodoPeriod)

    @Query("SELECT * FROM todo_period WHERE templateId = :templateId")
    suspend fun getTodoPeriodByTemplateId(templateId: Long): TodoPeriod?

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
}