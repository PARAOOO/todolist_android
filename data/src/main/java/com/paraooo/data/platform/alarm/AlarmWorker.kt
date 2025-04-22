package com.paraooo.data.platform.alarm

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paraooo.data.local.database.TodoDatabase
import com.paraooo.data.local.entity.TodoType
import com.paraooo.data.platform.handler.AlarmHandler
import com.paraooo.domain.model.Time
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
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