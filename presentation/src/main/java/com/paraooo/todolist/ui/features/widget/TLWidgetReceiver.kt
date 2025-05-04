package com.paraooo.todolist.ui.features.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.paraooo.todolist.ui.features.create.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class TLWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TLWidget


    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Received broadcast ${intent.action}")

        super.onReceive(context, intent)

        if (intent.action == "android.appwidget.action.APPWIDGET_UPDATE") {
            Log.d(TAG, "Received broadcast to update widget")

            // 업데이트 대상 위젯 ID 가져오기
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    val glanceIds = GlanceAppWidgetManager(context)
                        .getGlanceIds(TLWidget::class.java)

                    Log.d(TAG, "onReceive: id: ${glanceIds}")
                    glanceIds.forEach {
                        updateAppWidgetState(context, PreferencesGlanceStateDefinition, it) { prefs ->
                            prefs.toMutablePreferences().apply {
                                this[stringPreferencesKey("forceUpdateKey")] = System.currentTimeMillis().toString()
                            }
                        }
                        TLWidget.update(context, it)
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "onReceive: onError ${e}")
                }
            }
        }
    }
}