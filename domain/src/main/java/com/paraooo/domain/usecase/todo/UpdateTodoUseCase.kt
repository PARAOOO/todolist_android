package com.paraooo.domain.usecase.todo

import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.TodoType
import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.util.transferLocalDateToMillis

class UpdateTodoUseCase(
    private val todoRepository: TodoRepository,
    private val alarmScheduler: AlarmScheduler
) {

    suspend operator fun invoke(todo: TodoModel) {

        val instanceTodo = todoRepository.getTodoInstanceById(todo.instanceId) ?: return

        val todoTemplate = TodoTemplateModel(
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

        val todoInstance = TodoInstanceModel(
            id = todo.instanceId,
            templateId = instanceTodo.templateId,
            date = transferLocalDateToMillis(todo.date),
            progressAngle = todo.progressAngle
        )

        try {
            todoRepository.updateTodo(todoTemplate, todoInstance)

            if (todo.time != null) {
                when (todo.alarmType) {
                    AlarmType.OFF -> {}
                    AlarmType.NOTIFY, AlarmType.POPUP -> {
                        alarmScheduler.reschedule(todo.date, todo.time, instanceTodo.templateId)
                    }
                }
            } else {
                alarmScheduler.cancel(instanceTodo.templateId)
            }
        } catch (e : Exception) {
            TODO()
        }
    }
}