package com.paraooo.todolist.ui.features.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.paraooo.domain.usecase.todo.UpdateTodoProgressUseCase
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

        val updateTodoProgressUseCase : UpdateTodoProgressUseCase = GlobalContext.get().get()
        updateTodoProgressUseCase(todoId, updatedProgressAngle)
    }
}