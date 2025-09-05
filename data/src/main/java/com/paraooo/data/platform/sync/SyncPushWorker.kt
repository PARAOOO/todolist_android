package com.paraooo.data.platform.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.usecase.sync.SyncPushUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncPushWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val syncPushUseCase: SyncPushUseCase by inject()

    override suspend fun doWork(): Result {
        val result = syncPushUseCase()

        return when(result) {
            is UseCaseResult.Success -> Result.success()
            is UseCaseResult.Failure -> Result.retry()
            is UseCaseResult.Error -> Result.failure()
        }
    }

}