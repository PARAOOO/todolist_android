package com.paraooo.data.platform.alarm

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paraooo.data.local.database.TodoDatabase
import com.paraooo.data.local.entity.AlarmType
import com.paraooo.data.mapper.toModel
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

    private val alarmScheduler: AlarmScheduler by inject()

    override suspend fun doWork(): Result {

        val db = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo-database"
        ).build()

        val todayDateTimeMillis = LocalDateTime.now()
            .atZone(ZoneId.systemDefault()) // 시스템 시간대 적용
            .toInstant()
            .toEpochMilli()

        val todayLocalDate = LocalDate.now()
        val todayDateMillis = transferLocalDateToMillis(todayLocalDate)

        val alarmTodos = db.todoDao().getAlarmTodos(todayDateMillis)
        // todoType = GENERAL, alarmType != OFF, date >= today

        val alarmPeriodTodos = db.todoDao().getAlarmPeriodTodos(todayDateMillis)
        // todoType = PERIOD, alarmType != OFF, startDate <= today <= endDate

        val alarmDayOfWeekTodos = db.todoDao().getAlarmDayOfWeekTodos()
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

            Log.d(com.paraooo.data.repository.TAG, "postDayOfWeekTodo: ${alarmDate} ")

            alarmScheduler.schedule(
                date = alarmDate,
                time = Time(alarmDayOfWeekTodo.hour, alarmDayOfWeekTodo.minute),
                templateId = alarmDayOfWeekTodo.templateId
            )
        }

        return Result.success()
    }
}