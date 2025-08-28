package com.paraooo.data.platform.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.paraooo.domain.repository.AlarmScheduler
import com.paraooo.domain.util.todoToMillis
import java.time.LocalDate
import java.time.LocalTime


class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {

    override fun schedule(date: LocalDate, time: LocalTime, templateId : Long) {

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

    override fun reschedule(date: LocalDate, time: LocalTime, templateId: Long) {
        cancel(templateId) // 먼저 취소
        schedule(date, time, templateId) // 다시 등록
    }

    override fun cancel(templateId: Long) {
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