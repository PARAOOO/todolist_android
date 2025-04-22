package com.paraooo.data.platform.handler

import android.content.Context
import androidx.work.ListenableWorker.Result
import com.paraooo.data.local.dao.TodoDayOfWeekDao
import com.paraooo.data.local.dao.TodoInstanceDao
import com.paraooo.data.local.dao.TodoPeriodDao
import com.paraooo.data.local.dao.TodoTemplateDao
import com.paraooo.data.local.entity.TodoType
import com.paraooo.data.platform.alarm.AlarmScheduler
import com.paraooo.data.platform.alarm.NotificationHelper
import com.paraooo.domain.model.Time
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import java.time.LocalDate

class AlarmHandler(
    private val alarmScheduler: AlarmScheduler,
    private val notificationHelper: NotificationHelper,
    private val todoTemplateDao: TodoTemplateDao,
    private val todoInstanceDao: TodoInstanceDao,
    private val todoPeriodDao: TodoPeriodDao,
    private val todoDayOfWeekDao: TodoDayOfWeekDao
) {

    suspend fun handleAlarm(templateId : Long, context: Context) : Result {
        if (templateId == -1L) return Result.failure()

        val todayMillis = transferLocalDateToMillis(LocalDate.now())
        val todayLocalDate = LocalDate.now()

        val todoInstances = todoInstanceDao.getInstancesByTemplateId(templateId)
        val todoTemplate = todoTemplateDao.getTodoTemplateById(templateId) ?: return Result.failure()
        val period = todoPeriodDao.getTodoPeriodByTemplateId(templateId)
        val dayOfWeek = todoDayOfWeekDao.getDayOfWeekByTemplateId(templateId).takeIf { it.isNotEmpty() }


        val todayInstance = todoInstances.firstOrNull {
            transferMillis2LocalDate(it.date) == todayLocalDate
        }

//        Log.d(com.paraooo.data.platform.alarm.TAG, "AlarmWorker / todo : ${todoInstances}")
//        Log.d(com.paraooo.data.platform.alarm.TAG, "AlarmWorker / todo : ${todoTemplate}")
//        Log.d(com.paraooo.data.platform.alarm.TAG, "AlarmWorker / todo : ${period}")
//        Log.d(com.paraooo.data.platform.alarm.TAG, "AlarmWorker / todo : ${dayOfWeek}")

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
            notificationHelper.showNotification(context, todayInstance, todoTemplate)
        }

        return Result.success()
    }

}