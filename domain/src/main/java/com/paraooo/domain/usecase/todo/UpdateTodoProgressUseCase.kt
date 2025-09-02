package com.paraooo.domain.usecase.todo

import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoRepository
import java.util.UUID

class UpdateTodoProgressUseCase(
    private val todoRepository: TodoRepository
) {

    suspend operator fun invoke(instanceId: UUID, progress: Float): UseCaseResult<Unit> {
        try{
            todoRepository.updateTodoProgress(instanceId, progress)
            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(e)
        }
    }
}