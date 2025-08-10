package com.paraooo.domain.usecase.todo

import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.repository.TodoTemplateRepository

class DeleteTodoByIdUseCase(
    private val todoRepository: TodoRepository,
    private val todoInstanceRepository: TodoInstanceRepository,
    private val todoTemplateRepository: TodoTemplateRepository,
    private val alarmScheduler: AlarmScheduler
) {

    suspend operator fun invoke(instanceId: Long) {

        val instanceTodo = todoRepository.getTodoInstanceByInstanceId(instanceId) ?: return

//        val instanceTodo = todoInstanceRepository.getTodoInstanceById(instanceId)

//        todoTemplateRepository.deleteTodoTemplate(instanceTodo!!.templateId)

        try {
            todoRepository.deleteTodoTemplate(instanceTodo.templateId)
            alarmScheduler.cancel(templateId = instanceTodo.templateId)
        } catch (e : Exception) {

        }

    }
}