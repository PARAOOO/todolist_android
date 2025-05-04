package com.paraooo.todolist.ui.features.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TLWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TLWidget


    override fun onReceive(context: Context, intent: Intent) {

        super.onReceive(context, intent)

        if (intent.action == "UPDATE_TODOLIST_WIDGET") {
            Log.d("WidgetUpdateReceiver", "Received broadcast to update widget")

            // 업데이트 대상 위젯 ID 가져오기
            CoroutineScope(Dispatchers.IO).launch {
                val glanceIds = GlanceAppWidgetManager(context)
                    .getGlanceIds(TLWidget::class.java)
                glanceIds.forEach {
                    TLWidget.update(context, it)
                }
            }
        }
    }
}