package com.paraooo.data.platform.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "PARAOOO"

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val todoId = intent.getLongExtra("todoId", -1)

        Log.d(TAG, "AlarmReceiver - Alarm Received / todoId : ${todoId}")

        val workRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
            .setInputData(workDataOf("todoId" to todoId))
            .build()

        WorkManager.getInstance(context!!).enqueue(workRequest)
    }
}