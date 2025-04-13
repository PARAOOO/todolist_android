package com.paraooo.data.platform.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.paraooo.domain.model.TodoModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun todoToMillis(todo: TodoModel) : Long {
    val millis = LocalDateTime.of(
        todo.date.year,
        todo.date.month,
        todo.date.dayOfMonth,
        todo.time!!.hour,
        todo.time!!.minute
    ).atZone(ZoneId.systemDefault())  // 현지 시간 기준
        .toInstant()
        .toEpochMilli()

    val transferredTime: LocalDateTime = millis.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }
    Log.d(TAG, "todoToMillis: ${transferredTime}")

    return millis
}

class AlarmScheduler(
    private val context: Context
) {

    fun schedule(todo: TodoModel, instanceId : Long) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.data = Uri.parse("package:${context.packageName}")
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return
            }
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("todoId", instanceId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            instanceId.toInt(),  // 고유 키
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            todoToMillis(todo),
            pendingIntent
        )
    }

    fun reschedule(todo: TodoModel, instanceId: Long) {
        cancel(instanceId) // 먼저 취소
        schedule(todo, instanceId) // 다시 등록
    }

    fun cancel(instanceId: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("todoId", instanceId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            instanceId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "알람 취소됨: instanceId=$instanceId")
    }
}