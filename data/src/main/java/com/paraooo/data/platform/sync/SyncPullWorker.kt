package com.paraooo.data.platform.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paraooo.domain.model.UseCaseResult
import com.paraooo.domain.usecase.sync.SyncPullUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncPullWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val syncPullUseCase: SyncPullUseCase by inject()

    override suspend fun doWork(): Result {
        val result = syncPullUseCase()

        return when(result) {
            is UseCaseResult.Success -> Result.success()
            is UseCaseResult.Failure -> Result.retry()
            is UseCaseResult.Error -> Result.failure()
        }
    }

}