package com.paraooo.domain.usecase.todo

import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoTemplateRepository

class DeleteTodoByIdUseCase(
    private val todoInstanceRepository: TodoInstanceRepository,
    private val todoTemplateRepository: TodoTemplateRepository,
    private val alarmScheduler: AlarmScheduler
) {

    suspend operator fun invoke(instanceId: Long) {
        val instanceTodo = todoInstanceRepository.getTodoInstanceById(instanceId)

        todoTemplateRepository.deleteTodoTemplate(instanceTodo!!.templateId)

        alarmScheduler.cancel(templateId = instanceTodo.templateId)
    }
}