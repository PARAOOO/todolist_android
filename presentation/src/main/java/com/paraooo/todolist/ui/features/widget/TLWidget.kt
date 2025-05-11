package com.paraooo.todolist.ui.features.widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.paraooo.domain.model.TodoModel
import com.paraooo.domain.repository.TodoReadRepository
import com.paraooo.domain.repository.TodoWriteRepository
import com.paraooo.domain.util.transferLocalDateToMillis
import com.paraooo.todolist.MainActivity
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.features.create.TAG
import com.paraooo.todolist.ui.util.getDateWithDot
import kotlinx.coroutines.flow.first
import org.koin.core.context.GlobalContext
import java.time.LocalDate

object TLWidget: GlanceAppWidget() {

    override val stateDefinition = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        Log.d(TAG, "provideGlance: glanceID: ${id}")

        val todayDate = LocalDate.now()

        val todoReadRepository: TodoReadRepository = GlobalContext.get().get()
        val todoList = todoReadRepository.getTodoByDate(transferLocalDateToMillis(todayDate))
        val updatedList = todoList.first()

        provideContent {

            val prefs = currentState<Preferences>()
            val jsonTodoList = prefs[stringPreferencesKey("todoList")] ?: updatedList.toJson()
            val todoListState = jsonTodoList.toList()

            TLWidgetView(
                todoList = todoListState
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
        Row(
            modifier = GlanceModifier.fillMaxWidth().padding(start = 4.dp).padding(bottom = 4.dp),
            verticalAlignment = Alignment.Vertical.CenterVertically
        ){
            Text(
                text = "Today's Todos",
                style = TextStyle(
                    fontSize = 20.sp,
                    color = ColorProvider(Color(0xFF545454)),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                ),
                modifier = GlanceModifier.defaultWeight()
            )

            Image(
                provider = ImageProvider(R.drawable.ic_arrow_back_thin),
                contentDescription = null,
                modifier = GlanceModifier.defaultWeight().size(17.dp, 24.dp).padding(4.dp).clickable(
                    actionStartActivity(MainActivity::class.java)
                )
            )
        }

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
                text = getDateWithDot(LocalDate.now()),
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
                WidgetTodoCard(
                    index,
                    todo = todo
                )
            }
        }
    }
}