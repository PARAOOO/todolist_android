package com.paraooo.domain.usecase.sync

import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.repository.SyncRepository
import com.paraooo.domain.util.NetworkException
import java.util.UUID

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

            syncRepository.deleteTombstonesByIds(templatesToDelete.map { UUID.fromString(it.uuid) } + instancesToDelete.map { UUID.fromString(it.uuid) })

            return UseCaseResult.Success(Unit)
        } catch (e: NetworkException) {
            return UseCaseResult.Failure("네트워크 연결이 이상")
        } catch (e: Exception) {
            return UseCaseResult.Error(e)
        }

    }
}