package com.paraooo.data.platform.handler

import androidx.work.ListenableWorker.Result
import com.paraooo.domain.repository.TodoDayOfWeekRepository
import com.paraooo.domain.repository.TodoPeriodRepository
import com.paraooo.domain.repository.TodoTemplateRepository
import com.paraooo.domain.usecase.alarm.CalculateAlarmToRestoreUseCase
import com.paraooo.domain.usecase.alarm.ScheduleAlarmsUseCase
import com.paraooo.domain.util.todoToMillis
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class AlarmRestoreHandler(
    private val todoTemplateRepository: TodoTemplateRepository,
    private val todoPeriodRepository: TodoPeriodRepository,
    private val todoDayOfWeekRepository: TodoDayOfWeekRepository,
    private val calculateAlarmToRestoreUseCase: CalculateAlarmToRestoreUseCase,
    private val scheduleAlarmsUseCase: ScheduleAlarmsUseCase,
) {
    suspend fun handleAlarm() : Result {

        val todayLocalDate = LocalDate.now()
        val todayDateMillis = transferLocalDateToMillis(todayLocalDate)

        val alarmTodos = todoTemplateRepository.getAlarmTodos(todayDateMillis)
        // todoType = GENERAL, alarmType != OFF, date >= today
        val alarmPeriodTodos = todoPeriodRepository.getAlarmPeriodTodos(todayDateMillis)
        // todoType = PERIOD, alarmType != OFF, today <= endDate
        val alarmDayOfWeekTodos = todoDayOfWeekRepository.getAlarmDayOfWeekTodos()
        // todoType = DAYOFWEEK, alarmType != OFF

        val alarmSchedules = calculateAlarmToRestoreUseCase(
            alarmTodos = alarmTodos,
            alarmPeriodTodos = alarmPeriodTodos,
            alarmDayOfWeekTodos = alarmDayOfWeekTodos,
            todayLocalDate = LocalDate.now(),
            todayLocalDateTime = LocalDateTime.now(),
            todayLocalTime = LocalTime.now()
        )

        scheduleAlarmsUseCase(alarmSchedules)

        return Result.success()
    }
}