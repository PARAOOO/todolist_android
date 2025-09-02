package com.paraooo.data.platform.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.UUID

interface IntentProvider {
    fun getNotificationIntent(context: Context, todoId: UUID): PendingIntent
    fun getPopupIntent(context: Context): Intent
}