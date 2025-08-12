package com.paraooo.domain.usecase.todo

import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

class SyncDayOfWeekTodoUseCase(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(date: Long): UseCaseResult<Unit> {
        try{
            todoRepository.syncDayOfWeekInstance(date)
            return UseCaseResult.Success(Unit)
        }catch (e: Exception) {
            return UseCaseResult.Error(e)
        }
    }
}
