package com.paraooo.domain.usecase.alarm

import com.paraooo.domain.model.TodoDayOfWeekWithTimeModel
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.model.TodoPeriodWithTimeModel
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.util.todoToMillis
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.domain.util.transferMillis2LocalDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class CalculateAlarmToRestoreUseCase() {
    suspend operator fun invoke(
        todayLocalDateTime: LocalDateTime,
        todayLocalDate: LocalDate,
        todayLocalTime: LocalTime,
        alarmTodos: List<TodoModel>,
        alarmPeriodTodos: List<TodoPeriodWithTimeModel>,
        alarmDayOfWeekTodos: List<TodoDayOfWeekWithTimeModel>
    ): List<AlarmSchedule> {

        val alarmSchedules = mutableListOf<AlarmSchedule>()

        val todayDateTimeMillis = todayLocalDateTime
            .atZone(ZoneId.systemDefault()) // 시스템 시간대 적용
            .toInstant()
            .toEpochMilli()

        for (alarmTodo in alarmTodos) {

            val alarmMillis = todoToMillis(
                date = alarmTodo.date,
                time = alarmTodo.time!!
            )

            if(alarmMillis > todayDateTimeMillis){
                val alarmSchedule = AlarmSchedule(
                    date = alarmTodo.date,
                    time = alarmTodo.time,
                    templateId = alarmTodo.templateId
                )
                alarmSchedules.add((alarmSchedule))
            }
        }

        for (alarmPeriodTodo in alarmPeriodTodos) {

            // StartDateTime >= todayTime : startDate에 schedule
            // StartDateTime <= todayTime <= endDateTime
            //    if AlarmTime <= todayTime : today에 schedule
            //    else : today + 1에 schedule
            // endDateTime < todayTime : schedule 안함

            val todayDateTime = todayLocalDateTime
            val todayTime = todayLocalTime
            val alarmTime = LocalTime.of(alarmPeriodTodo.hour!!, alarmPeriodTodo.minute!!)

            val startDate = transferMillis2LocalDate(alarmPeriodTodo.startDate)
            val startDateTime = startDate.atTime(alarmPeriodTodo.hour, alarmPeriodTodo.minute)
            val endDate = transferMillis2LocalDate(alarmPeriodTodo.endDate)
            val endDateTime = endDate.atTime(alarmPeriodTodo.hour, alarmPeriodTodo.minute)

            if(startDateTime >= todayDateTime){
                val alarmSchedule = AlarmSchedule(
                    date = startDate,
                    time = LocalTime.of(alarmPeriodTodo.hour, alarmPeriodTodo.minute),
                    templateId = alarmPeriodTodo.templateId,
                )
                alarmSchedules.add(alarmSchedule)
            } else if(todayDateTime in startDateTime..endDateTime) {

                val isTimePassed = todayTime >= alarmTime

                val alarmDate = if (isTimePassed) todayLocalDate else todayLocalDate.plusDays(1)

                val alarmSchedule = AlarmSchedule(
                    date = alarmDate,
                    time = LocalTime.of(alarmPeriodTodo.hour, alarmPeriodTodo.minute),
                    templateId = alarmPeriodTodo.templateId,
                )
                alarmSchedules.add((alarmSchedule))
            }
        }

        for (alarmDayOfWeekTodo in alarmDayOfWeekTodos) {
            val availableDays = alarmDayOfWeekTodo.dayOfWeeks

            val today = todayLocalDate
            val now = todayLocalTime

            val todoTime = LocalTime.of(alarmDayOfWeekTodo.hour!!, alarmDayOfWeekTodo.minute!!)
            val isTimePassed = now > todoTime

            val startDayOffset = if (isTimePassed) 1 else 0

            val alarmDate = (startDayOffset..6).map { offset ->
                today.plusDays(offset.toLong())
            }.first { date ->
                availableDays.contains(date.dayOfWeek.value)
            }

            val alarmSchedule = AlarmSchedule(
                date = alarmDate,
                time = LocalTime.of(alarmDayOfWeekTodo.hour, alarmDayOfWeekTodo.minute),
                templateId = alarmDayOfWeekTodo.templateId
            )
            alarmSchedules.add(alarmSchedule)
        }

        return alarmSchedules
    }
}
