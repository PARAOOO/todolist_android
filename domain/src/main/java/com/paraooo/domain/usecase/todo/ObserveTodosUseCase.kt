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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest

class ObserveTodosUseCase(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(date: Long) : Flow<UseCaseResult<List<TodoModel>>> {
        val originalFlow = todoRepository.observeTodosByDate(date)

        return originalFlow.map {
            UseCaseResult.Success(it)
        }. catch { e ->
            UseCaseResult.Error(e as Exception)
        }
    }
}
