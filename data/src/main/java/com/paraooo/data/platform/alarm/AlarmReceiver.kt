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
        val templateId = intent.getLongExtra("templateId", -1)

        val workRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
            .setInputData(workDataOf("templateId" to templateId))
            .build()

        WorkManager.getInstance(context!!).enqueue(workRequest)
    }
}