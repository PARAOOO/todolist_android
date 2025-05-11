package com.paraooo.todolist.ui.features.widget

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.paraooo.domain.repository.TodoWriteRepository
import org.koin.core.context.GlobalContext

object TodoKey {
    val TodoId = ActionParameters.Key<Long>("todoId")
    val TodoProgressAngle = ActionParameters.Key<Float>("todoProgressAngle")
}

class TodoClickAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val todoId = parameters[TodoKey.TodoId] ?: return
        val todoProgressAngle = parameters[TodoKey.TodoProgressAngle] ?: return

        val updatedProgressAngle = if(todoProgressAngle >= 360F) 0F else 360F

        val todoWriteRepo: TodoWriteRepository = GlobalContext.get().get()
        todoWriteRepo.updateTodoProgress(todoId, updatedProgressAngle)
    }
}