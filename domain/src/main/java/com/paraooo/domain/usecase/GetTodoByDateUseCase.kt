package com.paraooo.domain.usecase

import com.paraooo.domain.model.TodoDayOfWeekModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoReadRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.util.transferMillis2LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import java.time.LocalDate

class GetTodoByDateUseCase(
    private val templateRepository: TodoTemplateRepository,
    private val instanceRepository: TodoInstanceRepository,
    private val dayOfWeekRepository: TodoDayOfWeekRepository
) {
    suspend operator fun invoke(date: Long): Flow<List<TodoModel>> {
        val targetDate = transferMillis2LocalDate(date)
        return templateRepository.observeTodosByDate(date)
            .transformLatest { currentList ->
                val existingTemplateIds = currentList.mapNotNull { it.instanceId }.toSet()
                val dayOfWeekTemplates: List<TodoDayOfWeekModel> =
                    dayOfWeekRepository.getDayOfWeekByTemplateId(date)
                val newTemplates =
                    dayOfWeekTemplates.filterNot { existingTemplateIds.contains(it.templateId) }
                if (newTemplates.isNotEmpty()) {
                    for (template in newTemplates) {
                        instanceRepository.insertTodoInstance(
                            TodoInstanceModel(
                                templateId = template.templateId,
                                date = targetDate
                            )
                        )
                    }
                    // The next DB emit will contain the new instances, so skip this emit.
                    return@transformLatest
                }
                emit(
                    currentList.sortedWith(
                        compareBy(
                            { it.time?.hour ?: Int.MAX_VALUE },
                            { it.time?.minute ?: Int.MAX_VALUE }
                        )
                    )
                )
            }
    }
}
