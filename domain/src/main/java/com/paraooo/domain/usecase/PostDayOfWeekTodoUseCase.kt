package com.paraooo.domain.usecase

import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.WidgetUpdater
import com.paraooo.domain.util.AlarmScheduler
import java.time.LocalDate
import java.time.LocalTime

class PostDayOfWeekTodoUseCase(
    private val templateRepository: TodoTemplateRepository,
    private val dayOfWeekRepository: TodoDayOfWeekRepository,
    private val alarmScheduler: AlarmScheduler,
    private val widgetUpdater: WidgetUpdater
) {
    suspend operator fun invoke(todo: TodoModel, dayOfWeek: List<Int>) {
        val templateId = templateRepository.insertTodoTemplate(
            TodoTemplateModel(
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
        for (week in dayOfWeek) {
            dayOfWeekRepository.insertTodoDayOfWeek(
                TodoDayOfWeekModel(
                    templateId = templateId,
                    dayOfWeeks = dayOfWeek,
                    dayOfWeek = week
                )
            )
        }
        if (todo.time != null && todo.alarmType != AlarmType.OFF) {
            val today = LocalDate.now()
            val now = LocalTime.now()
            val todoTime = LocalTime.of(todo.time.hour, todo.time.minute)
            val isTimePassed = now > todoTime
            val startDayOffset = if (isTimePassed) 1 else 0
            val alarmDate = (startDayOffset..6).map { offset ->
                today.plusDays(offset.toLong())
            }.first { date ->
                dayOfWeek.contains(date.dayOfWeek.value)
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
