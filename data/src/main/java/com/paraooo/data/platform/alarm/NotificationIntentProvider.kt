package com.paraooo.data.platform.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent

interface IntentProvider {
    fun getNotificationIntent(context: Context, todoId: Long): PendingIntent
    fun getPopupIntent(context: Context): Intent

}