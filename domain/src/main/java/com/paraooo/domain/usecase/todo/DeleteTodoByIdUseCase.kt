package com.paraooo.domain.usecase.todo

import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.repository.TodoTemplateRepository

class DeleteTodoByIdUseCase(
    private val todoRepository: TodoRepository,
    private val alarmScheduler: AlarmScheduler
) {

    suspend operator fun invoke(instanceId: Long) : UseCaseResult<Unit> {
        try {
            val instanceTodo = todoRepository.getTodoInstanceById(instanceId) ?: return UseCaseResult.Failure("id가 유효하지 않습니다.")

            todoRepository.deleteTodoTemplate(instanceTodo.templateId)
            alarmScheduler.cancel(templateId = instanceTodo.templateId)

            return UseCaseResult.Success(Unit)
        } catch (e : Exception) {
            return UseCaseResult.Error(e)
        }
    }
}