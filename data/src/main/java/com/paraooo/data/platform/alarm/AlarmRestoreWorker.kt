package com.paraooo.data.platform.alarm

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paraooo.data.platform.handler.AlarmRestoreHandler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlarmRestoreWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val alarmRestoreHandler: AlarmRestoreHandler by inject()

    override suspend fun doWork(): Result {

        return alarmRestoreHandler.handleAlarm()

    }
}