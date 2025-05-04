package com.paraooo.data.repository

import com.paraooo.data.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.data.datasource.TodoInstanceLocalDataSource
import com.paraooo.data.datasource.TodoPeriodLocalDataSource
import com.paraooo.data.datasource.TodoTemplateLocalDataSource
import com.paraooo.data.dto.TodoInstanceDto
import com.paraooo.data.mapper.toModel
import com.paraooo.data.platform.alarm.AlarmScheduler
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoReadRepository
import com.paraooo.domain.repository.WidgetUpdater
import com.paraooo.domain.util.transferMillis2LocalDate

class TodoReadRepositoryImpl(
    private val todoTemplateLocalDataSource : TodoTemplateLocalDataSource,
    private val todoInstanceLocalDataSource : TodoInstanceLocalDataSource,
    private val todoPeriodLocalDataSource : TodoPeriodLocalDataSource,
    private val todoDayOfWeekLocalDataSource : TodoDayOfWeekLocalDataSource,
) : TodoReadRepository {

    override suspend fun getTodoByDate(date: Long): List<TodoModel> {

        val todos = todoTemplateLocalDataSource.getTodosByDate(date)
        val dayOfWeekTemplates = todoDayOfWeekLocalDataSource.getDayOfWeekTodoTemplatesByDate(date)

        val templateIds = todos.map { it.templateId }.toSet()
        val filteredDayOfWeekTemplates = dayOfWeekTemplates.filterNot { templateIds.contains(it.id) }

        for (template in filteredDayOfWeekTemplates) {
            todoInstanceLocalDataSource.insertTodoInstance(
                TodoInstanceDto(
                    templateId = template.id,
                    date = date
                )
            )
        }

        val newInstances = todoTemplateLocalDataSource.getTodosByDate(date)

        return newInstances
            .sortedWith(compareBy({ it.hour ?: Int.MAX_VALUE }, { it.minute ?: Int.MAX_VALUE }))
            .map { it.toModel() }
    }

    override suspend fun findTodoById(instanceId: Long): TodoModel {
        val instance = todoInstanceLocalDataSource.getTodoInstanceById(instanceId)
        val template = todoTemplateLocalDataSource.getTodoTemplateById(instance!!.templateId)
        val period = todoPeriodLocalDataSource.getTodoPeriodByTemplateId(instance.templateId)
        val dayOfWeek = todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(instance.templateId).takeIf { it.isNotEmpty() }

        return TodoModel(
            instanceId = instance.id,
            title = template!!.title,
            description = template.description,
            date = transferMillis2LocalDate(instance.date),
            time = if (template.hour != null && template.minute != null) {
                Time(template.hour, template.minute)
            } else {
                null
            },
            alarmType = template.alarmType.toModel(),
            progressAngle = instance.progressAngle,
            startDate = period?.startDate?.let { transferMillis2LocalDate(it) },
            endDate = period?.endDate?.let { transferMillis2LocalDate(it) },
            dayOfWeeks = dayOfWeek?.map { it.dayOfWeek },
            isAlarmHasVibration = template.isAlarmHasVibration,
            isAlarmHasSound = template.isAlarmHasSound
        )
    }
}