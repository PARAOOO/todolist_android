package com.paraooo.todolist.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.paraooo.data.platform.alarm.IntentProvider
import com.paraooo.todolist.MainActivity
import com.paraooo.todolist.ui.features.alarm.AlarmActivity

class AppIntentProvider : IntentProvider {
    override fun getNotificationIntent(context: Context, todoId: Long): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("todo_id", todoId)  // 필요 시 데이터 전달
        }

        return PendingIntent.getActivity(
            context,
            todoId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun getPopupIntent(context: Context): Intent {
        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        return alarmIntent
    }
}