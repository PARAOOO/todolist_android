package com.paraooo.domain.usecase

import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.WidgetUpdater
import com.paraooo.domain.util.AlarmScheduler
import java.time.LocalDate
import java.time.LocalTime

class UpdateDayOfWeekTodoUseCase(
    private val instanceRepository: TodoInstanceRepository,
    private val templateRepository: TodoTemplateRepository,
    private val dayOfWeekRepository: TodoDayOfWeekRepository,
    private val alarmScheduler: AlarmScheduler,
    private val widgetUpdater: WidgetUpdater
) {
    suspend operator fun invoke(todo: TodoModel) {
        val instanceTodo = instanceRepository.getTodoInstanceById(todo.instanceId) ?: return
        val templateId = instanceTodo.templateId
        templateRepository.updateTodoTemplate(
            TodoTemplateModel(
                id = templateId,
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoType.DAY_OF_WEEK,
                alarmType = todo.alarmType,
                isAlarmHasVibration = todo.isAlarmHasVibration,
                isAlarmHasSound = todo.isAlarmHasSound
            )
        )
        val existingDayOfWeeks = dayOfWeekRepository.getDayOfWeekByTemplateId(templateId)
        val existingDaysSet = existingDayOfWeeks.map { it.dayOfWeek }.toSet()
        val newDaysSet = todo.dayOfWeeks!!.toSet()
        val daysToDelete = existingDaysSet - newDaysSet
        if (daysToDelete.isNotEmpty()) {
            dayOfWeekRepository.deleteSpecificDayOfWeeks(templateId, daysToDelete.toList())
            dayOfWeekRepository.deleteInstancesByTemplateIdAndDaysOfWeek(
                templateId,
                daysToDelete.toList()
            )
        }
        val daysToAdd = newDaysSet - existingDaysSet
        val newDayOfWeekModels = daysToAdd.map { dayOfWeek ->
            TodoDayOfWeekModel(
                templateId = templateId,
                dayOfWeeks = todo.dayOfWeeks!!,
                dayOfWeek = dayOfWeek
            )
        }
        dayOfWeekRepository.insertDayOfWeekTodos(newDayOfWeekModels)
        alarmScheduler.cancel(templateId)
        if (todo.time != null && todo.alarmType != AlarmType.OFF) {
            val today = LocalDate.now()
            val now = LocalTime.now()
            val todoTime = LocalTime.of(todo.time.hour, todo.time.minute)
            val isTimePassed = now > todoTime
            val startDayOffset = if (isTimePassed) 1 else 0
            val alarmDate = (startDayOffset..6).map { offset ->
                today.plusDays(offset.toLong())
            }.first { date ->
                todo.dayOfWeeks!!.contains(date.dayOfWeek.value)
            }
            alarmScheduler.schedule(
                date = alarmDate,
                time = todo.time,
                templateId = templateId
            )
        }
        widgetUpdater.updateWidget()
    }
}
