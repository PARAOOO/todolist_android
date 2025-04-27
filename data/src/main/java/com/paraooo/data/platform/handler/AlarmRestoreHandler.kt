package com.paraooo.data.platform.handler

import androidx.work.ListenableWorker.Result
import com.paraooo.data.datasource.TodoDayOfWeekLocalDataSource
import com.paraooo.data.datasource.TodoPeriodLocalDataSource
import com.paraooo.data.datasource.TodoTemplateLocalDataSource
import com.paraooo.data.platform.alarm.AlarmScheduler
import com.paraooo.data.platform.alarm.todoToMillis
import com.paraooo.domain.model.Time
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class AlarmRestoreHandler(
    private val alarmScheduler: AlarmScheduler,
    private val todoTemplateLocalDataSource: TodoTemplateLocalDataSource,
    private val todoPeriodLocalDataSource: TodoPeriodLocalDataSource,
    private val todoDayOfWeekLocalDataSource: TodoDayOfWeekLocalDataSource
) {
    suspend fun handleAlarm() : Result {

        val todayDateTimeMillis = LocalDateTime.now()
            .atZone(ZoneId.systemDefault()) // 시스템 시간대 적용
            .toInstant()
            .toEpochMilli()

        val todayLocalDate = LocalDate.now()
        val todayDateMillis = transferLocalDateToMillis(todayLocalDate)

        val alarmTodos = todoTemplateLocalDataSource.getAlarmTodos(todayDateMillis)
        // todoType = GENERAL, alarmType != OFF, date >= today
        val alarmPeriodTodos = todoPeriodLocalDataSource.getAlarmPeriodTodos(todayDateMillis)
        // todoType = PERIOD, alarmType != OFF, startDate <= today <= endDate
        val alarmDayOfWeekTodos = todoDayOfWeekLocalDataSource.getAlarmDayOfWeekTodos()
        // todoType = DAYOFWEEK, alarmType != OFF

        for (alarmTodo in alarmTodos) {

            val alarmMillis = todoToMillis(
                date = transferMillis2LocalDate(alarmTodo.date),
                time = Time(alarmTodo.hour!!, alarmTodo.minute!!)
            )

            if(alarmMillis > todayDateTimeMillis){
                alarmScheduler.schedule(
                    date = transferMillis2LocalDate(alarmTodo.date),
                    time = Time(alarmTodo.hour, alarmTodo.minute),
                    templateId = alarmTodo.templateId
                )
            }
        }

        for (alarmPeriodTodo in alarmPeriodTodos) {

            // StartDateTime >= todayTime : startDate에 schedule
            // StartDateTime <= todayTime <= endDateTime
            //    if AlarmTime <= todayTime : today에 schedule
            //    else : today + 1에 schedule
            // endDateTime < todayTime : schedule 안함

            val todayDateTime = LocalDateTime.now()
            val todayTime = LocalTime.now()
            val alarmTime = LocalTime.of(alarmPeriodTodo.hour!!, alarmPeriodTodo.minute!!)

            val startDate = transferMillis2LocalDate(alarmPeriodTodo.startDate)
            val startDateTime = startDate.atTime(alarmPeriodTodo.hour, alarmPeriodTodo.minute)
            val endDate = transferMillis2LocalDate(alarmPeriodTodo.endDate)
            val endDateTime = endDate.atTime(alarmPeriodTodo.hour, alarmPeriodTodo.minute)

            if(startDateTime >= todayDateTime){
                alarmScheduler.schedule(
                    date = startDate,
                    time = Time(alarmPeriodTodo.hour, alarmPeriodTodo.minute),
                    templateId = alarmPeriodTodo.templateId,
                )
            } else if(todayDateTime in startDateTime..endDateTime) {

                val isTimePassed = todayTime >= alarmTime

                val alarmDate = if (isTimePassed) todayLocalDate else todayLocalDate.plusDays(1)

                alarmScheduler.schedule(
                    date = alarmDate,
                    time = Time(alarmPeriodTodo.hour, alarmPeriodTodo.minute),
                    templateId = alarmPeriodTodo.templateId,
                )
            }
        }

        for (alarmDayOfWeekTodo in alarmDayOfWeekTodos) {
            val availableDays = alarmDayOfWeekTodo.dayOfWeeks

            val today = LocalDate.now()
            val now = LocalTime.now()

            val todoTime = LocalTime.of(alarmDayOfWeekTodo.hour!!, alarmDayOfWeekTodo.minute!!) // ⏰ 시간 조합
            val isTimePassed = now > todoTime

            val startDayOffset = if (isTimePassed) 1 else 0

            val alarmDate = (startDayOffset..6).map { offset ->
                today.plusDays(offset.toLong())
            }.first { date ->
                availableDays.contains(date.dayOfWeek.value)
            }

            alarmScheduler.schedule(
                date = alarmDate,
                time = Time(alarmDayOfWeekTodo.hour, alarmDayOfWeekTodo.minute),
                templateId = alarmDayOfWeekTodo.templateId
            )
        }

        return Result.success()
    }
}