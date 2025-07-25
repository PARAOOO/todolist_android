package com.paraooo.domain.usecase.todo

import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest

class GetTodoByDateUseCase(
    private val todoTemplateRepository: TodoTemplateRepository,
    private val todoDayOfWeekRepository: TodoDayOfWeekRepository,
    private val todoInstanceRepository: TodoInstanceRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(date: Long): Flow<List<TodoModel>> {
        return todoTemplateRepository.observeTodosByDate(date)
            .transformLatest { currentList ->
                // insert가 일어날 수도 있는 타이밍
                val existingTemplateIds = currentList.map { it.templateId }.toSet()
                val dayOfWeekTemplates = todoDayOfWeekRepository.getDayOfWeekTodoTemplatesByDate(date)
                val newTemplates = dayOfWeekTemplates.filterNot { existingTemplateIds.contains(it.id) }

                if (newTemplates.isNotEmpty()) {
                    for (template in newTemplates) {
                        todoInstanceRepository.insertTodoInstance(
                            TodoInstanceModel(templateId = template.id, date = date)
                        )
                    }
                    // insert 이후 DB가 갱신되면 observeTodosByDate가 다시 emit 하니까
                    // 이번 emit은 무시 (return)
                    return@transformLatest
                }

                // insert가 없어서 변화도 없으면 그냥 이걸 emit
                emit(currentList
                    .sortedWith(compareBy({ it.time?.hour ?: Int.MAX_VALUE }, { it.time?.minute ?: Int.MAX_VALUE }))
                )
            }
    }
}