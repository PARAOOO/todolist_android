package com.paraooo.domain.usecase

import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.WidgetUpdater
import com.paraooo.domain.util.AlarmScheduler

class UpdateTodoUseCase(
    private val templateRepository: TodoTemplateRepository,
    private val instanceRepository: TodoInstanceRepository,
    private val alarmScheduler: AlarmScheduler,
    private val widgetUpdater: WidgetUpdater
) {
    suspend operator fun invoke(todo: TodoModel) {
        val instanceTodo = instanceRepository.getTodoInstanceById(todo.instanceId)
        if (instanceTodo == null) return
        templateRepository.updateTodoTemplate(
            TodoTemplateModel(
                id = instanceTodo.templateId,
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = TodoType.GENERAL,
                alarmType = todo.alarmType,
                isAlarmHasVibration = todo.isAlarmHasVibration,
                isAlarmHasSound = todo.isAlarmHasSound
            )
        )
        instanceRepository.updateTodoInstance(
            TodoInstanceModel(
                id = todo.instanceId,
                templateId = instanceTodo.templateId,
                date = todo.date,
                progressAngle = todo.progressAngle
            )
        )
        if (todo.time != null) {
            when (todo.alarmType) {
                AlarmType.OFF -> alarmScheduler.cancel(instanceTodo.templateId)
                AlarmType.NOTIFY, AlarmType.POPUP -> {
                    alarmScheduler.reschedule(todo.date, todo.time, instanceTodo.templateId)
                }
            }
        } else {
            alarmScheduler.cancel(instanceTodo.templateId)
        }
        widgetUpdater.updateWidget()
    }
}
