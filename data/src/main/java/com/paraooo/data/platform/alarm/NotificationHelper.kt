package com.paraooo.data.platform.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.paraooo.data.R
import com.paraooo.data.local.entity.TodoInstance
import com.paraooo.data.local.entity.TodoTemplate
import com.paraooo.domain.model.TodoModel

object NotificationHelper {

    fun showNotification(context: Context, todoInstance: TodoInstance, todoTemplate: TodoTemplate) {
        val channelId = "todo_channel"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        Log.d(TAG, "showNotification: 1")
        
        // Android 8.0 이상 채널 생성 필요
        val channel = NotificationChannel(
            channelId,
            "todolist-notify",
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, channelId)
            .setColor(Color.parseColor("#FF54C392"))
            .setColorized(false)
            .setSmallIcon(R.drawable.bg_todolist_notification)
            .setContentTitle(todoTemplate.title)
            .setContentText("Todo 알림이 도착했습니다.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        Log.d(TAG, "showNotification: 2")

        manager.notify(todoInstance.id.toInt(), builder.build())
    }
}