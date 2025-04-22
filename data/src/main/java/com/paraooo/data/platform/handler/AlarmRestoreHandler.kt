package com.paraooo.data.platform.handler

import androidx.work.ListenableWorker.Result
import com.paraooo.data.local.dao.TodoDayOfWeekDao
import com.paraooo.data.local.dao.TodoPeriodDao
import com.paraooo.data.local.dao.TodoTemplateDao
import com.paraooo.data.local.entity.AlarmType
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
    private val todoTemplateDao: TodoTemplateDao,
    private val todoPeriodDao: TodoPeriodDao,
    private val todoDayOfWeekDao: TodoDayOfWeekDao
) {
    suspend fun handleAlarm() : Result {

        val todayDateTimeMillis = LocalDateTime.now()
            .atZone(ZoneId.systemDefault()) // 시스템 시간대 적용
            .toInstant()
            .toEpochMilli()

        val todayLocalDate = LocalDate.now()
        val todayDateMillis = transferLocalDateToMillis(todayLocalDate)

        val alarmTodos = todoTemplateDao.getAlarmTodos(todayDateMillis)
        // todoType = GENERAL, alarmType != OFF, date >= today
        val alarmPeriodTodos = todoPeriodDao.getAlarmPeriodTodos(todayDateMillis)
        // todoType = PERIOD, alarmType != OFF, startDate <= today <= endDate
        val alarmDayOfWeekTodos = todoDayOfWeekDao.getAlarmDayOfWeekTodos()
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

            val now = LocalTime.now()

            val todoTime = LocalTime.of(alarmPeriodTodo.hour!!, alarmPeriodTodo.minute!!) // ⏰ 시간 조합
            val isTimePassed = now > todoTime
            val endLocalDate = transferMillis2LocalDate(alarmPeriodTodo.endDate)

            if(isTimePassed) {
                if(endLocalDate > todayLocalDate){
                    val nextLocalDate = todayLocalDate.plusDays(1)
                    alarmScheduler.schedule(
                        date = nextLocalDate,
                        time = Time(alarmPeriodTodo.hour, alarmPeriodTodo.minute),
                        templateId = alarmPeriodTodo.templateId,
                    )
                }
            } else {
                alarmScheduler.schedule(
                    date = todayLocalDate,
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