package com.paraooo.data.platform.alarm

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paraooo.data.local.database.TodoDatabase
import com.paraooo.data.local.entity.TodoType
import com.paraooo.domain.util.transferLocalDateToMillis
import java.time.LocalDate

class AlarmWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val todoId = inputData.getLong("todoId", -1L)
        if (todoId == -1L) return Result.failure()

        val todayMillis = transferLocalDateToMillis(LocalDate.now())

        val db = Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo-database"
        ).build()

        val todoInstance = db.todoDao().getTodoInstanceById(todoId)
        val todoTemplate = db.todoDao().getTodoTemplateById(todoInstance!!.templateId) ?: return Result.failure()
        val period = db.todoDao().getTodoPeriodByTemplateId(todoInstance.templateId)
        val dayOfWeek = db.todoDao().getDayOfWeekByTemplateId(todoInstance.templateId).takeIf { it.isNotEmpty() }


        Log.d(TAG, "AlarmWorker / todo : ${todoInstance}")
        Log.d(TAG, "AlarmWorker / todo : ${todoTemplate}")

//        when(todoTemplate.type) {
//            TodoType.GENERAL -> {
//
//            }
//            TodoType.PERIOD -> {
//                if(period!!.endDate <= todayMillis) {
//
//                }
//            }
//            TodoType.DAY_OF_WEEK -> {}
//        }

        if (todoInstance.progressAngle < 360F){
            NotificationHelper.showNotification(applicationContext, todoInstance, todoTemplate)
        }
        // 다음 알람 예약 로직도 여기서
        return Result.success()
    }
}