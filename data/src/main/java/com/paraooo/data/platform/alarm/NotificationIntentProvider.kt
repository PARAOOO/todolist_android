package com.paraooo.data.platform.alarm

import android.app.PendingIntent
import android.content.Context

interface NotificationIntentProvider {
    fun getPendingIntent(context: Context, todoId: Long): PendingIntent
}