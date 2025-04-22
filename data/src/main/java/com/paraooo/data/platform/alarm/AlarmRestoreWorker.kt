package com.paraooo.data.platform.alarm

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paraooo.data.local.database.TodoDatabase
import com.paraooo.data.local.entity.AlarmType
import com.paraooo.data.mapper.toModel
import com.paraooo.data.platform.handler.AlarmRestoreHandler
import com.paraooo.domain.model.Time
import com.paraooo.domain.util.transferLocalDateTimeToMillis
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class AlarmRestoreWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params), KoinComponent {

    private val alarmRestoreHandler: AlarmRestoreHandler by inject()

    override suspend fun doWork(): Result {

        return alarmRestoreHandler.handleAlarm()

    }
}