package com.paraooo.todolist.ui.features.widget

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition

//class ToggleTodoAction(
//    private val index: Int
//) : ActionCallback {
//    override suspend fun onAction(
//        context: Context,
//        glanceId: GlanceId,
//        parameters: ActionParameters
//    ) {
//        updateAppWidgetState(context, glanceId) { prefs ->
//            val currentState = prefs[booleanPreferencesKey("todo_${index}")] ?: false
//
//        }
//    }
//
//}