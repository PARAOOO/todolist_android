package com.paraooo.domain.usecase.sync

import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.SyncRepository

class SyncPullUseCase(
    private val syncRepository: SyncRepository
) {

    suspend operator fun invoke(): UseCaseResult<Unit> {
        try {

            val syncPullResponse = syncRepository.syncPull()

            val (templatesToDelete, templatesToUpsert) = syncPullResponse.changedTemplates
                .partition { it.deleted }
            val (instancesToDelete, instancesToUpsert) = syncPullResponse.changedInstances
                .partition { it.deleted }

            syncRepository.handleSyncPullResponse(
                templatesToUpsert = templatesToUpsert,
                instancesToUpsert = instancesToUpsert,
                templatesToDelete = templatesToDelete,
                instancesToDelete = instancesToDelete,
                newSyncTimestamp = syncPullResponse.newSyncTimestamp
            )

            return UseCaseResult.Success(Unit)
        } catch (e: Exception) {
            return UseCaseResult.Error(e)
        }

    }
}