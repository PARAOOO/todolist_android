package com.paraooo.data.platform.alarm

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paraooo.data.local.database.TodoDatabase
import com.paraooo.data.local.entity.AlarmType
import com.paraooo.data.mapper.toModel
import com.paraooo.domain.util.transferLocalDateToMillis
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.time.LocalDateTime
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

        val todayDateMillis = transferLocalDateToMillis(LocalDate.now())

        val alarmTodos = db.todoDao().getAlarmTodos(todayDateMillis)
        // todoType = GENERAL, alarmType != OFF, date >= today

        for (alarmTodo in alarmTodos) {

            val alarmMillis = todoToMillis(alarmTodo.toModel())

            if(alarmMillis > todayDateTimeMillis){
                when (alarmTodo.alarmType) {
                    AlarmType.OFF -> {}
                    AlarmType.NOTIFY -> {
                        alarmScheduler.schedule(
                            todo = alarmTodo.toModel(),
                            instanceId = alarmTodo.instanceId
                        )
                    }

                    AlarmType.POPUP -> {}
                }
            }

        }
//        // 다음 알람 예약 로직도 여기서
        return Result.success()
    }
}