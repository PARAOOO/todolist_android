package com.paraooo.todolist.ui.features.widget

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.google.gson.Gson
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoReadRepository
import com.paraooo.domain.repository.TodoWriteRepository
import com.paraooo.domain.repository.WidgetUpdater
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.todolist.ui.features.create.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

val gson = Gson()

fun List<TodoModel>.toJson() : String {
    return gson.toJson(this)
}

fun String.toList() : List<TodoModel> {
    return gson.fromJson(this, Array<TodoModel>::class.java).toList()
}

class WidgetUpdaterImpl(
    private val context: Context,
    private val todoReadRepository: TodoReadRepository
) : WidgetUpdater {

    override suspend fun updateWidget() {

        val todayLocalDate = LocalDate.now()
        val todoList = todoReadRepository.getTodoByDate(transferLocalDateToMillis(todayLocalDate)).first()
        val jsonTodoList = todoList.toJson()

        try {
            val glanceIds = GlanceAppWidgetManager(context)
                .getGlanceIds(TLWidget::class.java)

            Log.d(TAG, "onReceive: id: ${glanceIds}")
            glanceIds.forEach {
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, it) { prefs ->
                    prefs.toMutablePreferences().apply {
                        this[stringPreferencesKey("todoList")] = jsonTodoList
                    }
                }
                TLWidget.update(context, it)
            }
        } catch (e: Exception) {
            Log.d(TAG, "onReceive: onError ${e}")
        }
    }
}