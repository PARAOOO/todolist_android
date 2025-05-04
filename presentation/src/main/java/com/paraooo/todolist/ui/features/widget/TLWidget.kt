package com.paraooo.todolist.ui.features.widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoRepository
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.todolist.R
import org.koin.core.context.GlobalContext
import java.time.LocalDate

object TLWidget: GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val todayDate = LocalDate.now()

        val repository: TodoRepository = GlobalContext.get().get()
        val todoList = repository.getTodoByDate(transferLocalDateToMillis(todayDate))
        val updatedList = todoList.map { it.copy() }

        provideContent {

            val prefs = currentState<Preferences>()
            val forceUpdateKey = longPreferencesKey("lastUpdated")
            val _lastUpdated = prefs[forceUpdateKey] // 상태 변화 유도용

            TLWidgetView(
                todoList = updatedList,
            )
        }
    }
}

@Composable
fun TLWidgetView(
    todoList : List<TodoModel>,
) {

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.White)
            .padding(12.dp)
    ) {
        Text(
            text = "Today's Todos",
            style = TextStyle(
                fontSize = 20.sp,
                color = ColorProvider(Color(0xFF545454)),
                fontWeight = FontWeight.Bold
            ),
            modifier = GlanceModifier.padding(start = 4.dp, top = 4.dp)
        )

        Spacer(
            modifier = GlanceModifier.height(18.dp)
        )

        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(
                    imageProvider = ImageProvider(R.drawable.bg_widget_todo_card_unchecked)
                ),
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(
                text = "2024. 1. 15",
                style = TextStyle(
                    fontSize = 12.sp,
                    color = ColorProvider(Color(0xFF7F7F7F)),
                    fontWeight = FontWeight.Normal
                ),
                modifier = GlanceModifier.padding(vertical = 6.dp)
            )
        }

        Spacer(
            modifier = GlanceModifier.height(4.dp)
        )

        LazyColumn{
            itemsIndexed(todoList) { index, todo ->

//                val checked = prefs[booleanPreferencesKey("todo_$index")] ?: false

                WidgetTodoCard(
                    index,
                    todo = todo
                )
            }
        }
    }
}