package com.paraooo.todolist.ui.features.home.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.paraooo.domain.model.TodoModel
import com.paraooo.todolist.R
import com.paraooo.todolist.ui.theme.PretendardFontFamily

const val TAG = "PARAOOO"

@Composable
fun TodoVerticalList(
    todoList : List<TodoModel>,
    onIsSwipedChanged: (
        todo : TodoModel, isSwiped : Boolean
    ) -> Unit,
    onProgressChanged : (
        todo : TodoModel, angle : Float
    ) -> Unit,
    onIsToggledOpenedChanged : (
        todo : TodoModel, isToggledOpened : Boolean
    ) -> Unit,
    onDeleteClicked : (
        todo : TodoModel
    ) -> Unit,
    onEditClicked : (
        todo : TodoModel
    ) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        when(todoList){

            emptyList<TodoModel>() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 150.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_logo_unchecked),
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "오늘의 Todo가 없습니다!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PretendardFontFamily,
                        color = Color(0xFFA6A6A6)
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(top = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    items(todoList) { todo: TodoModel ->
                        key(todo.instanceId, todo.progressAngle) {
                            SwipeableCard(
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(80.dp)
                                            .background(
                                                color = Color(0xFF529DFF),
                                                shape = RoundedCornerShape(
                                                    topEnd = 12.dp, // 오른쪽 위 모서리
                                                    bottomEnd = 12.dp, // 오른쪽 아래 모서리
                                                    topStart = 12.dp,
                                                    bottomStart = 12.dp
                                                )
                                            ),
                                        contentAlignment = Alignment.CenterEnd,
                                    ) {
                                        TodoBackgroundCard(
                                            modifier = Modifier
                                                .fillMaxWidth(1f - 0.7f)
                                                .fillMaxHeight(),
                                            onDeleteClicked = { onDeleteClicked(todo) },
                                            onEditClicked = { onEditClicked(todo) }
                                        )
                                    }

                                },
                                scrollRatio = 0.7f,
                                onIsSwipedChanged = { isSwiped: Boolean ->
                                    onIsSwipedChanged(todo, isSwiped)
                                    Log.d(
                                        TAG,
                                        "TodoVerticalList:onIsSwipedChanged : todoId : ${todo.instanceId} "
                                    )
                                },
                                isSwiped = todo.isSwiped,
                                todo = todo,
                                onProgressChanged = { angle: Float ->
                                    onProgressChanged(todo, angle)
                                },
                                onIsToggledOpenedChanged = { isToggledOpened: Boolean ->
                                    onIsToggledOpenedChanged(todo, isToggledOpened)
                                }
                            )
                        }
                    }

                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFF7F7F7),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun TodoVerticalListSkeleton() {
    Column {
        repeat(5) {
            TodoCardSkeleton()
        }
    }
}
