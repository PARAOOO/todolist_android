package com.paraooo.data.platform.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.paraooo.domain.model.Time
import com.paraooo.domain.model.TodoModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

fun todoToMillis(date : LocalDate, time : Time) : Long {
    val millis = LocalDateTime.of(
        date.year,
        date.month,
        date.dayOfMonth,
        time.hour,
        time.minute
    ).atZone(ZoneId.systemDefault())  // 현지 시간 기준
        .toInstant()
        .toEpochMilli()

    val transferredTime: LocalDateTime = millis.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    return millis
}

class AlarmScheduler(
    private val context: Context
) {

    fun schedule(date: LocalDate, time: Time, templateId : Long) {

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("templateId", templateId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            templateId.toInt(),  // 고유 키
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            todoToMillis(
                time = time,
                date = date
            ),
            pendingIntent
        )
    }

    fun reschedule(date: LocalDate, time: Time, templateId: Long) {
        cancel(templateId) // 먼저 취소
        schedule(date, time, templateId) // 다시 등록
    }

    fun cancel(templateId: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("templateId", templateId)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            templateId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "알람 취소됨: instanceId=$templateId")
    }
}