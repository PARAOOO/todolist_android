package com.paraooo.data.platform.alarm

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paraooo.data.platform.handler.AlarmHandler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate

class AlarmWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val alarmHandler : AlarmHandler by inject()

    override suspend fun doWork(): Result {
        val templateId = inputData.getLong("templateId", -1L)

        return alarmHandler.handleAlarm(templateId, applicationContext)

    }
}