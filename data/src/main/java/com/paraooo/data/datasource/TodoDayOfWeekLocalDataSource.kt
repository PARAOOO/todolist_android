package com.paraooo.data.datasource

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.paraooo.data.dto.TodoDayOfWeekDto
import com.paraooo.data.dto.TodoDayOfWeekWithTimeDto
import com.paraooo.data.dto.TodoTemplateDto
import com.paraooo.data.local.dao.TodoDayOfWeekDao
import com.paraooo.data.local.entity.TodoDayOfWeek
import com.paraooo.data.local.entity.TodoDayOfWeekWithTime
import com.paraooo.data.local.entity.TodoTemplate
import com.paraooo.data.mapper.toDto
import com.paraooo.data.mapper.toEntity

class TodoDayOfWeekLocalDataSource(
    private val todoDayOfWeekDao: TodoDayOfWeekDao
) {
    suspend fun insertTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeekDto) {
        todoDayOfWeekDao.insertTodoDayOfWeek(todoDayOfWeek.toEntity())
    }

    suspend fun insertDayOfWeekTodos(todoDayOfWeekEntities: List<TodoDayOfWeekDto>) {
        todoDayOfWeekDao.insertDayOfWeekTodos(todoDayOfWeekEntities.map { it.toEntity() })
    }

    suspend fun updateTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeekDto) {
        todoDayOfWeekDao.updateTodoDayOfWeek(todoDayOfWeek.toEntity())
    }

    suspend fun deleteTodoDayOfWeek(todoDayOfWeek: TodoDayOfWeekDto) {
        todoDayOfWeekDao.deleteTodoDayOfWeek(todoDayOfWeek.toEntity())
    }

    suspend fun getTodosByDayOfWeek(dayOfWeek: Int): List<TodoDayOfWeekDto> {
        return todoDayOfWeekDao.getTodosByDayOfWeek(dayOfWeek).map { it.toDto() }
    }

    suspend fun getDayOfWeekByTemplateId(templateId: Long): List<TodoDayOfWeekDto> {
        return todoDayOfWeekDao.getDayOfWeekByTemplateId(templateId).map { it.toDto() }
    }

    suspend fun deleteSpecificDayOfWeeks(templateId: Long, days: List<Int>) {
        todoDayOfWeekDao.deleteSpecificDayOfWeeks(templateId, days)
    }

    suspend fun deleteInstancesByTemplateIdAndDaysOfWeek(templateId: Long, days: List<Int>) {
        todoDayOfWeekDao.deleteInstancesByTemplateIdAndDaysOfWeek(templateId, days)
    }

    suspend fun getDayOfWeekTodoTemplatesByDate(date: Long): List<TodoTemplateDto> {
        return todoDayOfWeekDao.getDayOfWeekTodoTemplatesByDate(date).map { it.toDto() }
    }

    suspend fun getAlarmDayOfWeekTodos(): List<TodoDayOfWeekWithTimeDto> {
        return todoDayOfWeekDao.getAlarmDayOfWeekTodos().map { it.toDto() }
    }

}
