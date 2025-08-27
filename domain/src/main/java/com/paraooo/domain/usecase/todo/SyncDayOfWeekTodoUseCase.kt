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
    private val todoRepository: TodoRepository,
    private val todoTemplateRepository: TodoTemplateRepository,
    private val todoDayOfWeekRepository: TodoDayOfWeekRepository
) {
    suspend operator fun invoke(date: Long): UseCaseResult<Unit> {
        try{
            val currentList = todoTemplateRepository.getTodosByDate(date)

            val existingTemplateIds = currentList.map { it.templateId }.toSet()
            val dayOfWeekTemplates = todoDayOfWeekRepository.getDayOfWeekTodoTemplatesByDate(date)
            val newInstances = dayOfWeekTemplates.filterNot { existingTemplateIds.contains(it.id) }.map {
                TodoInstanceModel(templateId = it.id, date = date)
            }

            todoRepository.syncDayOfWeekInstance(newInstances)
            return UseCaseResult.Success(Unit)
        }catch (e: Exception) {
            return UseCaseResult.Error(e)
        }
    }
}
