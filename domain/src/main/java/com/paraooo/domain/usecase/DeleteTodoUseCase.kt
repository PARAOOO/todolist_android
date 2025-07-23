package com.paraooo.domain.usecase

import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.repository.WidgetUpdater
import com.paraooo.domain.util.AlarmScheduler

class DeleteTodoUseCase(
    private val instanceRepository: TodoInstanceRepository,
    private val templateRepository: TodoTemplateRepository,
    private val alarmScheduler: AlarmScheduler,
    private val widgetUpdater: WidgetUpdater
) {
    suspend operator fun invoke(instanceId: Long) {
        val instanceTodo = instanceRepository.getTodoInstanceById(instanceId) ?: return
        templateRepository.deleteTodoTemplate(instanceTodo.templateId)
        alarmScheduler.cancel(instanceTodo.templateId)
        widgetUpdater.updateWidget()
    }
}
