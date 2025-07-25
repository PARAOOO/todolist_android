package com.paraooo.domain.usecase.todo

import com.paraooo.domain.repository.TodoInstanceRepository

class UpdateTodoProgressUseCase(
    private val todoInstanceRepository: TodoInstanceRepository
) {

    suspend operator fun invoke(instanceId: Long, progress: Float) {
        todoInstanceRepository.updateTodoProgress(instanceId, progress)
    }
}