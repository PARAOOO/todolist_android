package com.paraooo.domain.usecase.todo

import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoRepository

class UpdateTodoProgressUseCase(
//    private val todoInstanceRepository: TodoInstanceRepository,
    private val todoRepository: TodoRepository
) {

    suspend operator fun invoke(instanceId: Long, progress: Float) {
        todoRepository.updateTodoProgress(instanceId, progress)
    }
}