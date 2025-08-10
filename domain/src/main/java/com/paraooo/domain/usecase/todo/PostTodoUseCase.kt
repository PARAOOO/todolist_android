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

class PostTodoUseCase(
//    private val todoTemplateRepository: TodoTemplateRepository,
//    private val todoInstanceRepository: TodoInstanceRepository,
    private val todoRepository: TodoRepository,
    private val alarmScheduler: AlarmScheduler,
) {

    suspend operator fun invoke(todo: TodoModel) {
        val todoTemplate = TodoTemplateModel(
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
            templateId = 0,
            date = transferLocalDateToMillis(todo.date)
        )
//
//        val templateId = todoTemplateRepository.insertTodoTemplate(todoTemplate)
//
//        val instanceId = todoInstanceRepository.insertTodoInstance(
//            TodoInstanceModel(
//                templateId = templateId,
//                date = transferLocalDateToMillis(todo.date)
//            )
//        )

        try {
            val templateId = todoRepository.postTodo(todoTemplate, todoInstance)

            if (todo.time != null) {
                when (todo.alarmType) {
                    AlarmType.OFF -> {}
                    AlarmType.NOTIFY, AlarmType.POPUP -> {
                        alarmScheduler.schedule(todo.date, todo.time, templateId)
                    }
                }
            }
        } catch (e : Exception) {
            TODO()
        }
    }
}