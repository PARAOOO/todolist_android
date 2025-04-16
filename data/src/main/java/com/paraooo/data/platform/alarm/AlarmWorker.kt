package com.paraooo.data.platform.alarm

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paraooo.data.local.database.TodoDatabase
import com.paraooo.data.local.entity.TodoType
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

    private val alarmScheduler: AlarmScheduler by inject()
    private val notificationHelper: NotificationHelper by inject()

    override suspend fun doWork(): Result {
        val templateId = inputData.getLong("templateId", -1L)
        if (templateId == -1L) return Result.failure()

        val todayMillis = transferLocalDateToMillis(LocalDate.now())
        val todayLocalDate = LocalDate.now()

        val db = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo-database"
        ).build()

        val todoInstances = db.todoDao().getInstancesByTemplateId(templateId)
        val todoTemplate = db.todoDao().getTodoTemplateById(templateId) ?: return Result.failure()
        val period = db.todoDao().getTodoPeriodByTemplateId(templateId)
        val dayOfWeek = db.todoDao().getDayOfWeekByTemplateId(templateId).takeIf { it.isNotEmpty() }


        val todayInstance = todoInstances.firstOrNull {
            transferMillis2LocalDate(it.date) == todayLocalDate
        }

        Log.d(TAG, "AlarmWorker / todo : ${todoInstances}")
        Log.d(TAG, "AlarmWorker / todo : ${todoTemplate}")
        Log.d(TAG, "AlarmWorker / todo : ${period}")
        Log.d(TAG, "AlarmWorker / todo : ${dayOfWeek}")

        // 5/1 ~ 5/3, 6:30
        // today : 5/3 , 6:30

        when(todoTemplate.type) {
            TodoType.GENERAL -> {

            }
            TodoType.PERIOD -> {
                if(period!!.endDate > todayMillis) {
                    val tomorrowLocalDate = todayLocalDate.plusDays(1)
                    alarmScheduler.schedule(
                        date = tomorrowLocalDate,
                        time = Time(todoTemplate.hour!!, todoTemplate.minute!!),
                        templateId = templateId
                    )
                }
            }
            TodoType.DAY_OF_WEEK -> {
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
            notificationHelper.showNotification(applicationContext, todayInstance, todoTemplate)
        }
        // 다음 알람 예약 로직도 여기서
        return Result.success()
    }
}