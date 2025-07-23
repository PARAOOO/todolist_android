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
import com.paraooo.domain.util.transferLocalDateToMillis
import java.time.LocalDate

class UpdatePeriodTodoUseCase(
    private val templateRepository: TodoTemplateRepository,
    private val instanceRepository: TodoInstanceRepository,
    private val periodRepository: TodoPeriodRepository,
    private val alarmScheduler: AlarmScheduler,
    private val widgetUpdater: WidgetUpdater
) {
    suspend operator fun invoke(todo: TodoModel, startDate: LocalDate, endDate: LocalDate) {
        val instanceTodo = instanceRepository.getTodoInstanceById(todo.instanceId) ?: return
        templateRepository.updateTodoTemplate(
            TodoTemplateModel(
                id = instanceTodo.templateId,
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
        periodRepository.updateTodoPeriod(
            TodoPeriodModel(
                templateId = instanceTodo.templateId,
                startDate = startDate,
                endDate = endDate
            )
        )
        val allDates = generateSequence(startDate) { it.plusDays(1) }
            .takeWhile { !it.isAfter(endDate) }
            .toList()
        // Remove existing which are not in new interval
        val existingInstances = instanceRepository.getInstancesByTemplateId(instanceTodo.templateId)
        val existingDateMap = existingInstances.associateBy { it.date }
        val newDates = allDates.toSet()
        val oldDates = existingDateMap.keys
        val datesToDelete = oldDates - newDates
        val datesToAdd = newDates - oldDates
        val datesToDeleteLong = datesToDelete.map { transferLocalDateToMillis(it) }.toSet()
        instanceRepository.deleteInstancesByDates(instanceTodo.templateId, datesToDeleteLong)
        val newInstances = datesToAdd.map { date ->
            TodoInstanceModel(templateId = instanceTodo.templateId, date = date, progressAngle = 0F)
        }
        instanceRepository.insertTodoInstances(newInstances)
        alarmScheduler.cancel(instanceTodo.templateId)
        if (todo.time != null && todo.alarmType != AlarmType.OFF) {
            val today = LocalDate.now()
            for (instance in existingInstances) {
                if (!instance.date.isBefore(today)) {
                    alarmScheduler.schedule(instance.date, todo.time, instanceTodo.templateId)
                    break
                }
            }
        }
        widgetUpdater.updateWidget()
    }
}
