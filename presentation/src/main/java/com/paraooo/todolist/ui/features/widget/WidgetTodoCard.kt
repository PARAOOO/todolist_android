package com.paraooo.todolist.ui.features.widget

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.paraooo.domain.model.TodoModel
import com.paraooo.todolist.R

@Composable
fun WidgetTodoCard(
    index: Int,
    todo : TodoModel,
) {

    Column{
        if(index != 0){
            Spacer(modifier = GlanceModifier.height(4.dp))
        }

        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(
                    imageProvider = ImageProvider(
                        if(todo.progressAngle >= 360) R.drawable.bg_widget_todo_card_checked
                        else R.drawable.bg_widget_todo_card_unchecked
                    )
                )
                .padding(12.dp)
//                .clickable (
//                    onClick = actionRunCallback<ToggleTodoAction>(
//                        actionParametersOf("index" to index.toString())
//                    )
//                )
            ,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Image(
                provider = ImageProvider(
                    if(todo.progressAngle >= 360) R.drawable.ic_logo_checked
                    else R.drawable.ic_logo_unchecked
                ),
                contentDescription = null,
                modifier = GlanceModifier
                    .size(36.dp)
            )

            Spacer(
                modifier = GlanceModifier.width(12.dp)
            )

            Column {
                Text(
                    text = todo.title,
                    maxLines = 1,  // 한 줄만 표시
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = ColorProvider(
                            if(todo.progressAngle >= 360) Color(0xFF54C392)
                            else Color(0xFF545454)
                        ),
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = GlanceModifier.height(2.dp))

                Text(
                    text = todo.description ?: "",
                    style = TextStyle(
                        fontSize = 8.sp,
                        color = ColorProvider(Color(0xFFA6A6A6)),
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }
}

