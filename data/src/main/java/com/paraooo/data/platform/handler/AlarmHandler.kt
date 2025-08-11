package com.paraooo.data.platform.handler

import android.content.Context
import android.content.Intent
import androidx.work.ListenableWorker.Result
import com.paraooo.data.platform.alarm.AlarmSchedulerImpl
import com.paraooo.data.platform.alarm.IntentProvider
import com.paraooo.data.platform.alarm.NotificationHelper
import com.paraooo.domain.model.AlarmType
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoInstanceRepository
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.usecase.alarm.CalculateNextAlarmUseCase
import com.paraooo.domain.usecase.alarm.ScheduleAlarmsUseCase
import com.paraooo.domain.util.transferMillis2LocalDate
import java.time.LocalDate

class AlarmHandler(
    private val alarmScheduler: AlarmSchedulerImpl,
    private val notificationHelper: NotificationHelper,
    private val calculateNextAlarmUseCase: CalculateNextAlarmUseCase,
    private val scheduleAlarmsUseCase: ScheduleAlarmsUseCase,
    private val todoInstanceRepository: TodoInstanceRepository,
    private val todoTemplateRepository: TodoTemplateRepository,
    private val todoPeriodRepository: TodoPeriodRepository,
    private val todoDayOfWeekRepository: TodoDayOfWeekRepository,
    private val intentProvider: IntentProvider,
) {
    suspend fun handleAlarm(templateId : Long, context: Context) : Result {
        if (templateId == -1L) return Result.failure()

        val todoInstances = todoInstanceRepository.getInstancesByTemplateId(templateId)
        val todoTemplate = todoTemplateRepository.getTodoTemplateById(templateId) ?: return Result.failure()
        val todoPeriod = todoPeriodRepository.getTodoPeriodByTemplateId(templateId)
        val todoDayOfWeek = todoDayOfWeekRepository.getDayOfWeekByTemplateId(templateId).takeIf { it.isNotEmpty() }

        val todayLocalDate = LocalDate.now()
        val todayInstance = todoInstances.firstOrNull {
            transferMillis2LocalDate(it.date) == todayLocalDate
        }

        val alarmSchedule = calculateNextAlarmUseCase(
            todoTemplate = todoTemplate,
            todoPeriod = todoPeriod,
            todoDayOfWeek = todoDayOfWeek,
            todayDate = todayLocalDate
        )

        if(alarmSchedule != null) {
            scheduleAlarmsUseCase(
                listOf(alarmSchedule)
            )
        }

//        val todayMillis = transferLocalDateToMillis(LocalDate.now())
//        val todayLocalDate = LocalDate.now()
//
//        val todoInstances = todoInstanceLocalDataSource.getInstancesByTemplateId(templateId)
//        val todoTemplate = todoTemplateLocalDataSource.getTodoTemplateById(templateId) ?: return Result.failure()
//        val period = todoPeriodLocalDataSource.getTodoPeriodByTemplateId(templateId)
//        val dayOfWeek = todoDayOfWeekLocalDataSource.getDayOfWeekByTemplateId(templateId).takeIf { it.isNotEmpty() }
//
//
//
//        when(todoTemplate.type) {
//            TodoTypeEntity.GENERAL -> {
//
//            }
//            TodoTypeEntity.PERIOD -> {
//                if(period!!.endDate > todayMillis) {
//                    val tomorrowLocalDate = todayLocalDate.plusDays(1)
//                    alarmScheduler.schedule(
//                        date = tomorrowLocalDate,
//                        time = Time(todoTemplate.hour!!, todoTemplate.minute!!),
//                        templateId = templateId
//                    )
//                }
//            }
//            TodoTypeEntity.DAY_OF_WEEK -> {
//                val availableDays = dayOfWeek!!.first().dayOfWeeks
//
//                val nextAlarmDate = (1..7)
//                    .map { todayLocalDate.plusDays(it.toLong()) }
//                    .first { availableDays.contains(it.dayOfWeek.value) }
//
//                alarmScheduler.schedule(
//                    date = nextAlarmDate,
//                    time = Time(todoTemplate.hour!!, todoTemplate.minute!!),
//                    templateId = templateId
//                )
//            }
//        }

        if (todayInstance != null && todayInstance.progressAngle < 360F){
            when(todoTemplate.alarmType) {
                AlarmType.OFF -> {}
                AlarmType.NOTIFY -> notificationHelper.showNotification(context, todayInstance, todoTemplate)
                AlarmType.POPUP -> {
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