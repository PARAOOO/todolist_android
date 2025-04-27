package com.paraooo.data.platform.handler

import android.content.Context
import android.content.Intent
import androidx.work.ListenableWorker.Result
import com.paraooo.data.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.data.datasource.TodoInstanceLocalDataSource
import com.paraooo.data.datasource.TodoPeriodLocalDataSource
import com.paraooo.data.datasource.TodoTemplateLocalDataSource
import com.paraooo.data.dto.AlarmTypeDto
import com.paraooo.data.dto.TodoTypeDto
import com.paraooo.data.platform.alarm.AlarmScheduler
import com.paraooo.data.platform.alarm.IntentProvider
import com.paraooo.data.platform.alarm.NotificationHelper
import com.paraooo.domain.model.Time
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import java.time.LocalDate

class AlarmHandler(
    private val alarmScheduler: AlarmScheduler,
    private val notificationHelper: NotificationHelper,
    private val todoTemplateLocalDataSource: TodoTemplateLocalDataSource,
    private val todoInstanceLocalDataSource: TodoInstanceLocalDataSource,
    private val todoPeriodLocalDataSource: TodoPeriodLocalDataSource,
    private val todoDayOfWeekLocalDataSource: TodoDayOfWeekLocalDataSource,
    private val intentProvider: IntentProvider,
) {
    suspend fun handleAlarm(templateId : Long, context: Context) : Result {
        if (templateId == -1L) return Result.failure()

        val todayMillis = transferLocalDateToMillis(LocalDate.now())
        val todayLocalDate = LocalDate.now()

        val todoInstances = todoInstanceLocalDataSource.getInstancesByTemplateId(templateId)
        val todoTemplate = todoTemplateLocalDataSource.getTodoTemplateById(templateId) ?: return Result.failure()
        val period = todoPeriodLocalDataSource.getTodoPeriodByTemplateId(templateId)
        val dayOfWeek = todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(templateId).takeIf { it.isNotEmpty() }

        val todayInstance = todoInstances.firstOrNull {
            transferMillis2LocalDate(it.date) == todayLocalDate
        }

        when(todoTemplate.type) {
            TodoTypeDto.GENERAL -> {

            }
            TodoTypeDto.PERIOD -> {
                if(period!!.endDate > todayMillis) {
                    val tomorrowLocalDate = todayLocalDate.plusDays(1)
                    alarmScheduler.schedule(
                        date = tomorrowLocalDate,
                        time = Time(todoTemplate.hour!!, todoTemplate.minute!!),
                        templateId = templateId
                    )
                }
            }
            TodoTypeDto.DAY_OF_WEEK -> {
                val availableDays = dayOfWeek!!.first().dayOfWeeks

                val nextAlarmDate = (1..7)
                    .map { todayLocalDate.plusDays(it.toLong()) }
                    .first { availableDays.contains(it.dayOfWeek.value) }

                alarmScheduler.schedule(
                    date = nextAlarmDate,
                    time = Time(todoTemplate.hour!!, todoTemplate.minute!!),
                    templateId = templateId
                )
            }
        }

        if (todayInstance != null && todayInstance.progressAngle < 360F){
            when(todoTemplate.alarmType) {
                AlarmTypeDto.OFF -> {}
                AlarmTypeDto.NOTIFY -> notificationHelper.showNotification(context, todayInstance, todoTemplate)
                AlarmTypeDto.POPUP -> {
                    val intent = intentProvider.getPopupIntent(context)
                    intent.putExtra("instanceId", todayInstance.id)  // 여기서 데이터를 전달
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
        }
        // 다음 알람 예약 로직도 여기서
        return Result.success()
    }
}