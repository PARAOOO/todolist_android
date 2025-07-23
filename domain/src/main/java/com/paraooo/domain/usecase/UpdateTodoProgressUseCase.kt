package com.paraooo.domain.usecase

import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.WidgetUpdater

class UpdateTodoProgressUseCase(
    private val instanceRepository: TodoInstanceRepository,
    private val widgetUpdater: WidgetUpdater
) {
    suspend operator fun invoke(instanceId: Long, progress: Float) {
        instanceRepository.updateTodoProgress(instanceId, progress)
        widgetUpdater.updateWidget()
    }
}
