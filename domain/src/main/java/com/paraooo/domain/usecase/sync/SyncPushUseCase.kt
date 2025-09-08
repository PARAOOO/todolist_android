package com.paraooo.domain.usecase.sync

import com.paraooo.domain.model.TodoInstanceModel
import com.paraooo.domain.model.TodoTemplateModel
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.SyncRepository
import com.paraooo.domain.util.NetworkException
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

            if(templatesNeedsSync.isNotEmpty() || instancesNeedsSync.isNotEmpty() || templatesDeleted.isNotEmpty() || instancesDeleted.isNotEmpty()) {
                syncRepository.syncPush(
                    templates = templatesNeedsSync,
                    instances = instancesNeedsSync,
                    deletedTemplateIds = templatesDeleted,
                    deletedInstanceIds = instancesDeleted
                )
            }

            return UseCaseResult.Success(Unit)
        } catch (e: NetworkException) {
            return UseCaseResult.Failure("네트워크 연결이 이상")
        } catch (e: Exception) {
            return UseCaseResult.Error(e)
        }
    }
}