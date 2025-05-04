package com.paraooo.todolist.ui.features.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.paraooo.domain.repository.WidgetUpdater
import com.paraooo.todolist.ui.features.create.TAG


class WidgetUpdaterImpl(
    private val context: Context
) : WidgetUpdater {

    override suspend fun updateWidget() {

        val intent = Intent("UPDATE_TODOLIST_WIDGET")
        context.sendBroadcast(intent)

//        Log.d(TAG, "updateWidget: ")
//
//        TLWidget.updateAll(context)
//
//        val glanceIds = GlanceAppWidgetManager(context).getGlanceIds(TLWidget::class.java)
//        Log.d(TAG, "Glance IDs: ${glanceIds}")
//
//        GlanceAppWidgetManager(context)
//            .getGlanceIds(TLWidget.javaClass)
//            .forEach {
//                Log.d(TAG, "updateWidget: ${it}")
//                TLWidget.update(context, it)
//            }
    }
}