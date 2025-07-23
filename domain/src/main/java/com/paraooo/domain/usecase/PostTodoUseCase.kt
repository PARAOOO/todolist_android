package com.paraooo.domain.usecase

import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.WidgetUpdater
import com.paraooo.domain.util.AlarmScheduler
import java.time.LocalDate

class PostTodoUseCase(
    private val templateRepository: TodoTemplateRepository,
    private val instanceRepository: TodoInstanceRepository,
    private val alarmScheduler: AlarmScheduler,
    private val widgetUpdater: WidgetUpdater
) {
    suspend operator fun invoke(todo: TodoModel) {
        val templateId = templateRepository.insertTodoTemplate(
            com.paraooo.domain.model.TodoTemplateModel(
                title = todo.title,
                description = todo.description ?: "",
                hour = todo.time?.hour,
                minute = todo.time?.minute,
                type = com.paraooo.domain.model.TodoType.GENERAL,
                alarmType = todo.alarmType,
                isAlarmHasVibration = todo.isAlarmHasVibration,
                isAlarmHasSound = todo.isAlarmHasSound
            )
        )
        instanceRepository.insertTodoInstance(
            com.paraooo.domain.model.TodoInstanceModel(
                templateId = templateId,
                date = todo.date
            )
        )
        if (todo.time != null && (todo.alarmType == AlarmType.NOTIFY || todo.alarmType == AlarmType.POPUP)) {
            alarmScheduler.schedule(todo.date, todo.time, templateId)
        }
        widgetUpdater.updateWidget()
    }
}