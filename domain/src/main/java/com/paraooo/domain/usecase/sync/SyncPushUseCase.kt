package com.paraooo.domain.usecase.sync

import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.SyncRepository
import java.util.UUID

class SyncPushUseCase(
    private val syncRepository: SyncRepository
) {

    suspend operator fun invoke(): UseCaseResult<Unit> {
        try {

            val templatesNeedsSync = syncRepository.getUnsyncedTemplates()
            val instancesNeedsSync = syncRepository.getUnsyncedInstances()
            val instancesDeleted = syncRepository.getDeletedInstances()
            val templatesDeleted = syncRepository.getDeletedTemplates()

            syncRepository.syncPush(
                templates = templatesNeedsSync,
                instances = instancesNeedsSync,
                deletedTemplateIds = templatesDeleted,
                deletedInstanceIds = instancesDeleted
            )

            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(e)
        }
    }
}