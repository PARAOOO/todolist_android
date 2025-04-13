package com.paraooo.data.platform.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager

class AlarmBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // 여기에 다시 알람 등록하는 로직 작성
            // 예: DB에서 저장된 Todo들을 읽어와서 alarmScheduler.schedule(todo, id) 호출

            val request = OneTimeWorkRequestBuilder<AlarmRestoreWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST) // 빠르게 실행 요청
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }
    }
}