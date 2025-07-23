package com.paraooo.domain.usecase

import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoPeriodModel
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.repository.WidgetUpdater
import com.paraooo.domain.util.AlarmScheduler
import java.time.LocalDate

class PostPeriodTodoUseCase(
    private val templateRepository: TodoTemplateRepository,
    private val instanceRepository: TodoInstanceRepository,
    private val periodRepository: TodoPeriodRepository,
    private val alarmScheduler: AlarmScheduler,
    private val widgetUpdater: WidgetUpdater
) {
    suspend operator fun invoke(todo: TodoModel, startDate: LocalDate, endDate: LocalDate) {
        val templateId = templateRepository.insertTodoTemplate(
            TodoTemplateModel(
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoType.PERIOD,
                alarmType = todo.alarmType,
                isAlarmHasVibration = todo.isAlarmHasVibration,
                isAlarmHasSound = todo.isAlarmHasSound
            )
        )
        val allDates = generateSequence(startDate) { it.plusDays(1) }
            .takeWhile { !it.isAfter(endDate) }
            .toList()
        val todos = allDates.map { date ->
            TodoInstanceModel(
                templateId = templateId,
                date = date
            )
        }
        instanceRepository.insertTodoInstances(todos)
        periodRepository.insertTodoPeriod(
            TodoPeriodModel(
                templateId = templateId,
                startDate = startDate,
                endDate = endDate
            )
        )
        if (todo.time != null && todo.alarmType != AlarmType.OFF) {
            val today = LocalDate.now()
            for (date in allDates) {
                if (!date.isBefore(today)) {
                    alarmScheduler.schedule(date, todo.time, templateId)
                    break
                }
            }
        }
        widgetUpdater.updateWidget()
    }
}
